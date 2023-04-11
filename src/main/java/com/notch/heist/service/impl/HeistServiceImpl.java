package com.notch.heist.service.impl;

import com.notch.heist.domain.Heist;
import com.notch.heist.domain.HeistSkill;
import com.notch.heist.domain.Member;
import com.notch.heist.domain.Skill;
import com.notch.heist.domain.enums.HeistStatus;
import com.notch.heist.mapper.HeistMapper;
import com.notch.heist.repository.HeistRepository;
import com.notch.heist.repository.HeistSkillRepository;
import com.notch.heist.rest.dto.*;
import com.notch.heist.rest.errors.HeistStatusException;
import com.notch.heist.service.EmailService;
import com.notch.heist.service.HeistService;
import com.notch.heist.service.MemberService;
import com.notch.heist.service.SkillService;
import com.notch.heist.service.jobs.ChangeHeistStatusJob;
import com.notch.heist.service.util.Util;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeistServiceImpl implements HeistService {

    private final HeistMapper heistMapper;
    private final MemberService memberService;
    private final SkillService skillService;
    private final HeistRepository heistRepository;
    private final HeistSkillRepository heistSkillRepository;
    private final EmailService emailService;
    private final Util util;
    private final Scheduler scheduler;

    public HeistServiceImpl(HeistMapper heistMapper, HeistRepository heistRepository, HeistSkillRepository heistSkillRepository, MemberService memberService, SkillService skillService, EmailService emailService, Util util, Scheduler scheduler) {
        this.heistMapper = heistMapper;
        this.heistRepository = heistRepository;
        this.heistSkillRepository = heistSkillRepository;
        this.memberService = memberService;
        this.skillService = skillService;
        this.emailService = emailService;
        this.util = util;
        this.scheduler = scheduler;
    }

    @Override
    public Heist getHeist(Long heisId) {
        return heistRepository.findById(heisId)
                .orElseThrow(() -> new NoSuchElementException("Heist with id " + heisId + " does not exist"));
    }

    @Override
    public Heist addHeist(HeistDTO heistDTO) throws SchedulerException {
        Assert.isTrue(heistRepository.countByName(heistDTO.getName()) == 0,
                "Heist with name " + heistDTO.getName() + " already exists");
        Assert.isTrue(heistDTO.getEndTime().isAfter(heistDTO.getStartTime()),
                "Heist end time is after heist start time");
        Assert.isTrue(heistDTO.getEndTime().isBefore(ZonedDateTime.now()),
                "Heist end time is in the past");
        Assert.isTrue(heistDTO.getSkills().stream().map(HeistSkillDTO::getName).collect(Collectors.toSet()).size()
                == heistDTO.getSkills().size(), "Skill with same name appeared multiple times");

        Heist heist = heistMapper.toHeist(heistDTO);
        heist.setStatus(HeistStatus.PLANNING);

        heist = heistRepository.save(heist);
        Set<HeistSkill> heistSkillSet = heist.getSkills();

        for (HeistSkillDTO heistSkillDTO : heistDTO.getSkills()) {
            Skill newSkill;

            if (!skillService.skillExists(heistSkillDTO.getName())) {
                newSkill = skillService.createSkill(heistSkillDTO.getName());
            } else {
                newSkill = skillService.getSkill(heistSkillDTO.getName());
            }

            HeistSkill heistSkill = new HeistSkill();
            heistSkill.setHeist(heist);
            heistSkill.setHeistId(heist.getId());
            heistSkill.setSkill(newSkill);
            heistSkill.setSkillId(newSkill.getId());
            heistSkill.setLevel(heistSkillDTO.getLevel());
            heistSkill.setMembersCount(heistSkillDTO.getMembers());

            heistSkillSet.add(heistSkill);
        }

        heist.getSkills().addAll(heistSkillSet);

        heist = heistRepository.save(heist);

        scheduleJob(ChangeHeistStatusJob.class, heist.getStartTime(), heist.getId(), HeistStatus.IN_PROGRESS,  "start_heist_" + heist.getId());
        scheduleJob(ChangeHeistStatusJob.class, heist.getEndTime(), heist.getId(), HeistStatus.FINISHED, "end_heist_" + heist.getId());

        return heist;
    }

    @Override
    public Heist updateHeist(Long heistId, HeistKillWrapperDTO heistKillWrapperDTO) {
        Heist heist = getHeist(heistId);

        Assert.isTrue(heistKillWrapperDTO.getSkills().stream().map(skill -> skill.getName() + skill.getLevel()).distinct().count() > 0,
                "Skill with same name and level appeared multiple times");

        if (heist.getStartTime().isBefore(ZonedDateTime.now())) {
            throw new HeistStatusException("Heist with id " + heistId + " has already started");
        }

        Set<HeistSkill> heistSkillSet = heist.getSkills();

        for (HeistSkillDTO heistSkillDTO : heistKillWrapperDTO.getSkills()) {

            if (!skillService.skillExists(heistSkillDTO.getName())) {
                Skill newSkill = skillService.createSkill(heistSkillDTO.getName());

                HeistSkill heistSkill = new HeistSkill();
                heistSkill.setHeist(heist);
                heistSkill.setHeistId(heist.getId());
                heistSkill.setSkill(newSkill);
                heistSkill.setSkillId(newSkill.getId());
                heistSkill.setLevel(heistSkillDTO.getLevel());
                heistSkill.setMembersCount(heistSkillDTO.getMembers());

                heistSkillSet.add(heistSkill);
            } else {
                if (heistSkillSet.stream()
                        .map(HeistSkill::getSkill)
                        .collect(Collectors.toList()).contains(heistSkillDTO.getName())
                ) {
                    heistSkillSet.stream()
                            .filter(heistSkill -> heistSkill.getSkill().getName().equals(heistSkillDTO.getName()))
                            .forEach(heistSkill -> {
                                heistSkill.setLevel(heistSkillDTO.getLevel());
                                heistSkill.setMembersCount(heistSkillDTO.getMembers());
                            });
                } else {
                    Skill newSkill = skillService.getSkill(heistSkillDTO.getName());

                    HeistSkill heistSkill = new HeistSkill();
                    heistSkill.setHeist(heist);
                    heistSkill.setHeistId(heist.getId());
                    heistSkill.setSkill(newSkill);
                    heistSkill.setSkillId(newSkill.getId());
                    heistSkill.setLevel(heistSkillDTO.getLevel());
                    heistSkill.setMembersCount(heistSkillDTO.getMembers());

                    heistSkillSet.add(heistSkill);
                }
            }
        }

        heistSkillRepository.saveAll(heistSkillSet);

        return heist;
    }

    @Override
    public HeistStatusDTO getHeistStatus(Long heistId) {
        Heist heist = getHeist(heistId);

        HeistStatusDTO heistStatusDTO = new HeistStatusDTO();
        heistStatusDTO.setStatus(heist.getStatus());

        return heistStatusDTO;
    }

    @Override
    public Set<HeistSkill> getHeistSkills(Long heistId) {
        Heist heist = getHeist(heistId);

        return heist.getSkills();
    }

    @Override
    public EligibleMembersDTO getEligibleMembers(Long heistId) {
        Heist heist = getHeist(heistId);

        if (heist.getStartTime().isBefore(ZonedDateTime.now())) {
            throw new HeistStatusException("Heist with id " + heistId + " has already started");
        }

        EligibleMembersDTO eligibleMembersDTO = new EligibleMembersDTO();

        Set<MemberDTO> availableMembers = memberService.getMembersAvailable(heist.getStartTime(), heist.getEndTime());
        Set<MemberDTO> eligibleMembers = new HashSet<>();
        Set<HeistSkillDTO> heistSkillDTOSet = new HashSet<>();

        for (HeistSkill heistSkill : heist.getSkills()) {
            for (MemberDTO memberDTO : availableMembers) {
                Set<HeistSkillDTO> contributingSkills = memberService.getHeistContributingSkills(memberDTO, heistSkill);
                if (contributingSkills.size() > 0) {
                    eligibleMembers.add(memberDTO);
                    heistSkillDTOSet.addAll(contributingSkills);
                }
            }
        }

        eligibleMembersDTO.setMembers(eligibleMembers.stream().toList());
        eligibleMembersDTO.setSkills(heistSkillDTOSet.stream().toList());

        return eligibleMembersDTO;
    }

    @Override
    public Heist confirmMembers(Long heistId, HeistMembersDTO heistMembersDTO) {
        Heist heist = getHeist(heistId);

        if (heist.getStatus() != HeistStatus.PLANNING) {
            throw new HeistStatusException("Heist with id " + heistId + " has already started");
        }

        Set<Member> heistMembers = memberService.getMembersByNames(heistMembersDTO.getMembers());

        heist.setMembers(heistMembers);
        heist.setStatus(HeistStatus.READY);

        heist = heistRepository.save(heist);

        heistMembers.forEach(member -> {
            emailService.sendMessage(member.getEmail(), "HEIST STATUS", "You have been confirmed as heist member!");
        });

        return heist;
    }

    @Override
    public void changeHeistStatus(Long heistId, String heistStatus) {
        Heist heist = getHeist(heistId);

        heist.setStatus(HeistStatus.valueOf(heistStatus));

        if (HeistStatus.valueOf(heistStatus) == HeistStatus.FINISHED) {
            heist.getMembers().forEach(member -> {
                emailService.sendMessage(member.getEmail(), "HEIST FINISHED", "Heist you are a part of has finished!");
            });
        }

        heistRepository.save(heist);
    }

    @Override
    public HeistOutcomeDTO setHeistOutcome(Long heistId, Heist heist) {
        HeistStatus heistStatus;

        int requiredMembersNumber = heist.getSkills().stream()
                .map(HeistSkill::getMembersCount)
                .reduce(0, Integer::sum)
                .intValue();

        int accomplishedMembersNumber = heist.getMembers().size();

        double requiredMembersPercentage = util.calculatePercentage(accomplishedMembersNumber, requiredMembersNumber);
        Set<Member> membersSet = heist.getMembers();

        if (requiredMembersPercentage < 50.0) {
            heistStatus = HeistStatus.FAILED;
            memberService.updateMembersAfterHeist(membersSet, membersSet.size());
        } else if (requiredMembersPercentage >= 50.0 && requiredMembersPercentage < 75.0) {
            if (Math.random() > 0.5) {
                heistStatus = HeistStatus.FAILED;
                memberService.updateMembersAfterHeist(membersSet, (int)(((double)(membersSet.size() * 2)) / 3.0));
            } else {
                heistStatus = HeistStatus.SUCCEEDED;
                memberService.updateMembersAfterHeist(membersSet, (int)(((double)(membersSet.size() * 1)) / 3.0));
            }
        } else if (requiredMembersPercentage >= 75.0 && requiredMembersPercentage < 100.0) {
            heistStatus = HeistStatus.SUCCEEDED;
            memberService.updateMembersAfterHeist(membersSet, (int)(((double)(membersSet.size() * 1)) / 3.0));
        } else {
            heistStatus = HeistStatus.SUCCEEDED;
        }

        HeistOutcomeDTO heistOutcomeDTO = new HeistOutcomeDTO();
        heistOutcomeDTO.setOutcome(heistStatus);

        heist.setStatus(heistStatus);
        heistRepository.save(heist);

        memberService.updateMembersSkillAfterHeist(membersSet, heist.getSkills(), (int) heist.getStartTime().until(heist.getEndTime(), ChronoUnit.SECONDS));

        return heistOutcomeDTO;
    }

    @Override
    public HeistOutcomeDTO getHeistOutcome(Long heistId) {
        Heist heist = getHeist(heistId);

        if (heist.getStatus() == HeistStatus.FINISHED) {
            setHeistOutcome(heistId, heist);
        }

        HeistOutcomeDTO heistOutcomeDTO = new HeistOutcomeDTO();
        heistOutcomeDTO.setOutcome(heist.getStatus());

        return heistOutcomeDTO;
    }

    @Override
    public void startHeist(Long heistId) {
        Heist heist = getHeist(heistId);

        if (heist.getStatus() != HeistStatus.READY) {
            throw new HeistStatusException("Heist with id " + heistId + " is not ready");
        }

        heist.setStatus(HeistStatus.IN_PROGRESS);

        heist.getMembers().forEach(member -> {
            emailService.sendMessage(member.getEmail(), "HEIST STARTED", "Heist you are a part of has started!");
        });

        heistRepository.save(heist);
    }

    @Override
    public List<Member> getHeistMembers(Long heistId) {
        Heist heist = getHeist(heistId);

        if (heist.getStatus() == HeistStatus.PLANNING) {
            throw new HeistStatusException("Heist with id " + heistId + " is in planning");
        }

        return heist.getMembers().stream().toList();
    }

    private SimpleTrigger createTrigger(ZonedDateTime eventTime, String event) {
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(event, "group1")
                .startAt(Date.from(eventTime.toInstant()))
                .forJob(event, "group1")
                .build();

        return trigger;
    }

    private JobDetail createJob(Class jobClass, Long heistId, HeistStatus heistStatus, String event) {
        JobDetail job = JobBuilder
                .newJob(jobClass)
                .withIdentity(event, "group1")
                .usingJobData("heist_id", heistId)
                .usingJobData("heist_status", heistStatus.toString())
                .build();

        return job;
    }

    private void scheduleJob(Class jobClass, ZonedDateTime eventTime, Long heistId, HeistStatus heistStatus, String event) throws SchedulerException {
        scheduler.scheduleJob(
                createJob(jobClass, heistId, heistStatus, event),
                createTrigger(eventTime, event)
        );

        scheduler.start();
    }
}