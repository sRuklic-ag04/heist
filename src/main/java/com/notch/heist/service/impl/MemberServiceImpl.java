package com.notch.heist.service.impl;

import com.notch.heist.domain.HeistSkill;
import com.notch.heist.domain.Member;
import com.notch.heist.domain.MemberSkill;
import com.notch.heist.domain.Skill;
import com.notch.heist.domain.enums.MemberStatus;
import com.notch.heist.mapper.MemberMapper;
import com.notch.heist.repository.MemberRepository;
import com.notch.heist.repository.MemberSkillRepository;
import com.notch.heist.repository.SkillRepository;
import com.notch.heist.rest.dto.HeistSkillDTO;
import com.notch.heist.rest.dto.MemberDTO;
import com.notch.heist.rest.dto.MemberSkillsDTO;
import com.notch.heist.rest.dto.SkillDTO;
import com.notch.heist.service.EmailService;
import com.notch.heist.service.MemberService;
import com.notch.heist.service.SkillService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    @Value("${heist.levelUpTime}")
    private long levelUpTime;

    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final SkillService skillService;
    private final MemberSkillRepository memberSkillRepository;
    private final EmailService emailService;
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository, SkillRepository skillRepository, MemberSkillRepository memberSkillRepository, SkillService skillService, MemberSkillRepository memberSkillRepository1, EmailService emailService, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.skillRepository = skillRepository;
        this.skillService = skillService;
        this.memberSkillRepository = memberSkillRepository1;
        this.emailService = emailService;
        this.memberMapper = memberMapper;
    }

    @Override
    public Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member with id " + memberId + " does not exist"));

        return member;
    }

    @Override
    public Member addMember(MemberDTO memberDTO) {
        Assert.isTrue(memberDTO.getSkills().stream().map(SkillDTO::getName).collect(Collectors.toSet()).size()
        == memberDTO.getSkills().size(), "Skill with same name appeared multiple times");
        Assert.isTrue(memberRepository.countByName(memberDTO.getName()) == 0,
                "Member with name " + memberDTO.getName() + " already exists");
        Assert.isTrue(memberRepository.countByEmail(memberDTO.getEmail()) == 0,
                "Member with email " + memberDTO.getEmail() + " already exists");

        Member member = memberMapper.toMember_(memberDTO);

        member = memberRepository.save(member);
        Set<MemberSkill> memberSkillSet = new HashSet<>();

        for (SkillDTO skillDTO : memberDTO.getSkills()) {
            Skill newSkill;

            if (!skillService.skillExists(skillDTO.getName())) {
                newSkill = skillService.createSkill(skillDTO.getName());
            } else {
                newSkill = skillService.getSkill(skillDTO.getName());
            }

            MemberSkill memberSkill = new MemberSkill();
            memberSkill.setMember(member);
            memberSkill.setMemberId(member.getId());
            memberSkill.setSkill(newSkill);
            memberSkill.setSkillId(newSkill.getId());
            memberSkill.setLevel(skillDTO.getLevel());

            memberSkillSet.add(memberSkill);
        }

        member.setSkills(memberSkillSet);

        member = memberRepository.save(member);

        emailService.sendMessage(member.getEmail(), "MEMBER PROFILE CREATED", "Your member profile has been successfully created!");

        return member;
    }

    @Override
    public Member updateMember(Long memberId, MemberSkillsDTO memberSkillsDTO) {
        Member member = getMember(memberId);

        Set<MemberSkill> memberSkillSet = member.getSkills();

        for (SkillDTO skillDTO : memberSkillsDTO.getSkills()) {

            // skill didn't exist add it to member
            if (!skillService.skillExists(skillDTO.getName())) {
                Skill newSkill = skillService.createSkill(skillDTO.getName());

                MemberSkill memberSkill = new MemberSkill();
                memberSkill.setLevel(skillDTO.getLevel());
                memberSkill.setSkill(newSkill);
                memberSkill.setSkillId(newSkill.getId());
                memberSkill.setMember(member);
                memberSkill.setMemberId(member.getId());

                memberSkillSet.add(memberSkill);
            } else {
                // update skill or add existing
                if (memberSkillSet.stream()
                        .map(MemberSkill::getSkill)
                        .collect(Collectors.toList()).contains(skillDTO.getName())
                ) {
                    memberSkillSet.stream()
                            .filter(memberSkill -> memberSkill.getSkill().getName().equals(skillDTO.getName()))
                            .forEach(memberSkill -> memberSkill.setLevel(skillDTO.getLevel()));
                } else {
                    Skill newSkill = skillService.getSkill(skillDTO.getName());

                    MemberSkill memberSkill = new MemberSkill();
                    memberSkill.setLevel(skillDTO.getLevel());
                    memberSkill.setSkill(newSkill);
                    memberSkill.setSkillId(newSkill.getId());
                    memberSkill.setMember(member);
                    memberSkill.setMemberId(member.getId());
                    memberSkillSet.add(memberSkill);
                }
            }
        }

        memberSkillRepository.saveAll(memberSkillSet);

        return member;
    }

    @Override
    @Transactional
    public void deleteSkill(Long memberId, String skillName) {
        Member member = getMember(memberId);

        Skill skill = skillRepository.findByName(skillName)
                .orElseThrow(() -> new NoSuchElementException("Member with id " + memberId + " does not posses skill" + skillName));

        if (member.getSkills().stream()
                .map(MemberSkill::getSkill)
                .filter(skill_ -> skill_.getName().equals(skillName)).count() <= 0) {
            throw new NoSuchElementException();
        }

        memberSkillRepository.deleteByMemberIdAndSkillId(memberId, skill.getId());
    }

    @Override
    public Set<MemberDTO> getMembersAvailable(ZonedDateTime startDate, ZonedDateTime endDate) {
        List<Member> availableMembers = memberRepository.findAvailableMembers(startDate, endDate);

        return availableMembers.stream().map(member -> memberMapper.toDTO(member)).collect(Collectors.toSet());
    }

    @Override
    public Set<HeistSkillDTO> getHeistContributingSkills(MemberDTO memberDTO, HeistSkill heistSkill) {

        Set<HeistSkillDTO> heistSkillDTOSet = memberDTO.getSkills().stream()
                .filter(skill -> skill.getLevel().length() >= heistSkill.getLevel().length()
                        && skill.getName().equals(heistSkill.getSkill().getName())
                ).map(
                        skill -> {
                            HeistSkillDTO heistSkillDTO = new HeistSkillDTO();
                            heistSkillDTO.setMembers(heistSkill.getMembersCount());
                            heistSkillDTO.setName(heistSkill.getSkill().getName());
                            heistSkillDTO.setLevel(heistSkill.getLevel());
                            return heistSkillDTO;
                        }
                )
                .collect(Collectors.toSet());

        return heistSkillDTOSet;
    }

    @Override
    public Set<Member> getMembersByNames(List<String> names) {
        Set<Member> members = memberRepository.findByNameIn(names).stream()
                .collect(Collectors.toSet());

        return members;
    }

    @Override
    public void changeMemberStatus(Long memberId, MemberStatus memberStatus) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException());

        member.setStatus(memberStatus);

        memberRepository.save(member);
    }

    @Override
    public void updateMembersAfterHeist(Set<Member> memberSet, int amountOfMembersToUpdate) {
        memberSet.stream()
                .limit(amountOfMembersToUpdate)
                .map(member -> {
                    member.setStatus(Math.random() > 0.5 ? MemberStatus.EXPIRED : MemberStatus.INCARCERATED);
                    return memberRepository.save(member);
                }).collect(Collectors.toSet());
    }

    @Override
    public void updateMembersSkillAfterHeist(Set<Member> membersSet, Set<HeistSkill> skills, int seconds) {
        for (Member member : membersSet) {
            member.getSkills().stream()
                    .filter(memberSkill -> skills.stream()
                            .anyMatch(heistSkill -> heistSkill.getSkill().getName().equals(memberSkill.getSkill().getName())))
                    .forEach(memberSkill -> {
                        int totalTimeSpentOnHeist = memberSkill.getTimeSpentOnHeist() + seconds;
                        memberSkill.setTimeSpentOnHeist(totalTimeSpentOnHeist);
                        if (totalTimeSpentOnHeist >= levelUpTime) {
                            int levelsToAdd = (int) (totalTimeSpentOnHeist / levelUpTime);
                            memberSkill.setTimeSpentOnHeist((int) (totalTimeSpentOnHeist % levelUpTime));
                            memberSkill.setLevel(memberSkill.getLevel() + "*".repeat(levelsToAdd));
                            memberSkill.setLevel(memberSkill.getLevel().substring(0, Math.min(memberSkill.getLevel().length(), 10)));
                        }
                    });
            memberRepository.save(member);
        }
    }
}
