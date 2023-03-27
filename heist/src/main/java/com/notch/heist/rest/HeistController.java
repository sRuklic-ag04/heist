package com.notch.heist.rest;

import com.notch.heist.domain.Heist;
import com.notch.heist.domain.HeistSkill;
import com.notch.heist.mapper.HeistMapper;
import com.notch.heist.mapper.HeistSkillMapper;
import com.notch.heist.mapper.MemberMapper;
import com.notch.heist.repository.MemberRepository;
import com.notch.heist.rest.dto.*;
import com.notch.heist.service.HeistService;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/heist")
public class HeistController {

    private final HeistService heistService;
    private final HeistMapper heistMapper;
    private final MemberMapper memberMapper;
    private final HeistSkillMapper heistSkillMapper;
    private final MemberRepository memberRepository;


    public HeistController(HeistService heistService, HeistMapper heistMapper, MemberMapper memberMapper, HeistSkillMapper heistSkillMapper,
                           MemberRepository memberRepository) {
        this.heistService = heistService;
        this.heistMapper = heistMapper;
        this.memberMapper = memberMapper;
        this.heistSkillMapper = heistSkillMapper;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/{heist_id}")
    public ResponseEntity<HeistDTO> getHeist(@PathVariable("heist_id") Long heistId) {
        Heist heist = heistService.getHeist(heistId);

        return ResponseEntity.ok(heistMapper.toDTO(heist));
    }

    @PostMapping
    public ResponseEntity<?> addHeist(@RequestBody HeistDTO heistDTO) throws SchedulerException {
        Heist heist = heistService.addHeist(heistDTO);

        return ResponseEntity.created(URI.create("/heist/" + heist.getId())).build();
    }

    @PatchMapping("/{heist_id}/skills")
    public ResponseEntity<?> updateHeistSkills(@PathVariable("heist_id") Long heistId, @RequestBody HeistKillWrapperDTO skills) {
        Heist heist = heistService.updateHeist(heistId, skills);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + heist.getId() + "/skills").build();
    }

    @GetMapping("/{heist_id}/eligible_members")
    public ResponseEntity<EligibleMembersDTO> getEligibleMembers(@PathVariable("heist_id") Long heistId) {
        EligibleMembersDTO members = heistService.getEligibleMembers(heistId);

        return ResponseEntity.ok(members);
    }

    @PutMapping("/{heist_id}/members")
    public ResponseEntity<?> confirmMembers(@PathVariable("heist_id") Long heistId, @RequestBody HeistMembersDTO heistMembersDTO) {
        Heist heist = heistService.confirmMembers(heistId, heistMembersDTO);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + heist.getId() + "/members").build();
    }

    @PutMapping("/{heist_id}/start")
    public ResponseEntity<?> startHeist(@PathVariable("heist_id") Long heistId) {
        heistService.startHeist(heistId);

        return ResponseEntity.ok().location(URI.create("/heist/" + heistId + "/status")).build();
    }

    @GetMapping("/{heist_id}/members")
    public ResponseEntity<List<MemberDTO>> getHeistMembers(@PathVariable("heist_id") Long heistId) {
        List<MemberDTO> memberDTOList = heistService.getHeistMembers(heistId).stream()
                .map(memberMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(memberDTOList);
    }

    @GetMapping("/{heist_id}/skills")
    public ResponseEntity<List<HeistSkillDTO>> getHeistSkills(@PathVariable("heist_id") Long heistId) {
        Set<HeistSkill> heistSkillDTOList = heistService.getHeistSkills(heistId);

        return ResponseEntity.ok(heistSkillMapper.toDTOList(heistSkillDTOList));
    }

    @GetMapping("/{heist_id}/status")
    public ResponseEntity<HeistStatusDTO> getHeistStatus(@PathVariable("heist_id") Long heistId) {
        HeistStatusDTO heistStatusDTO = heistService.getHeistStatus(heistId);

        return ResponseEntity.ok(heistStatusDTO);
    }

    @GetMapping("/{heist_id}/outcome")
    public ResponseEntity<HeistOutcomeDTO> getHeistOutcome(@PathVariable("heist_id") Long heistId) {
        HeistOutcomeDTO heistOutcomeDTO = heistService.getHeistOutcome(heistId);

        return ResponseEntity.ok(heistOutcomeDTO);
    }
}
