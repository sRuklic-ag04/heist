package com.notch.heist.rest;

import com.notch.heist.domain.Member;
import com.notch.heist.mapper.MemberMapper;
import com.notch.heist.rest.dto.MemberDTO;
import com.notch.heist.rest.dto.MemberSkillsDTO;
import com.notch.heist.service.MemberService;
import com.notch.heist.service.MemberSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final MemberSkillService memberSkillService;

    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberSkillService memberSkillService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberSkillService = memberSkillService;
        this.memberMapper = memberMapper;
    }

    @GetMapping("/{member_id}")
    public ResponseEntity<?> getMember(@PathVariable("member_id") Long memberId) {
        Member member = memberService.getMember(memberId);

        return ResponseEntity.ok(memberMapper.toDTO(member));
    }

    @PostMapping
    public ResponseEntity<Member> addMember(@RequestBody MemberDTO memberDTO) {
        Member savedMember = memberService.addMember(memberDTO);

        return ResponseEntity.created(URI.create("/member/" + savedMember.getId())).build();
    }

    @PutMapping("/{member_id}/skills")
    public ResponseEntity<?> updateMemberSkill(@PathVariable("member_id") Long memberId, @RequestBody MemberSkillsDTO memberSkillsDTO) {
        memberService.updateMember(memberId, memberSkillsDTO);

        return ResponseEntity.noContent().header("Content-Location", "/member/" + memberId + "/skills").build();
    }

    @DeleteMapping("/{member_id}/skills/{skill_name}")
    public ResponseEntity<?> deleteMemberSkill(@PathVariable("member_id") Long memberId, @PathVariable("skill_name") String skillName) {
        memberService.deleteSkill(memberId, skillName);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{member_id}/skills")
    public ResponseEntity<MemberSkillsDTO> getMemberSkills(@PathVariable("member_id") Long memberId) {
        MemberSkillsDTO memberSkillsDTO = memberSkillService.getMemberSkills(memberId);

        return ResponseEntity.ok(memberSkillsDTO);
    }
}
