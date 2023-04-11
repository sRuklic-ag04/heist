package com.notch.heist.mapper;

import com.notch.heist.domain.Member;
import com.notch.heist.domain.MemberSkill;
import com.notch.heist.domain.Skill;
import com.notch.heist.rest.dto.MemberDTO;
import com.notch.heist.rest.dto.SkillDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MemberMapper {

    private final SkillMapper skillMapper;

    public MemberMapper(SkillMapper skillMapper) {
        this.skillMapper = skillMapper;
    }

    public Member toMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setSex(memberDTO.getSex());
        member.setEmail(memberDTO.getEmail());
        member.setStatus(memberDTO.getStatus());

        Skill mainSkill = new Skill();
        mainSkill.setName(memberDTO.getMainSkill());
//        member.setMainSkill(mainSkill);

        Set<MemberSkill> memberSkillSet = new HashSet<>();
        for (SkillDTO skillDTO : memberDTO.getSkills()) {
            MemberSkill memberSkill = new MemberSkill();
            memberSkill.setSkill(new Skill(skillDTO.getName()));
            memberSkill.setLevel(skillDTO.getLevel());
            memberSkill.setMember(member);

            memberSkillSet.add(memberSkill);
        }

        member.setSkills(memberSkillSet);

        return member;
    }

    public Member toMember_(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setSex(memberDTO.getSex());
        member.setEmail(memberDTO.getEmail());
        member.setStatus(memberDTO.getStatus());

        return member;
    }

    public MemberDTO toDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setName(member.getName());
        memberDTO.setSex(member.getSex());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setStatus(member.getStatus());
        memberDTO.setSkills(skillMapper.toDTOList(member.getSkills()));

        return memberDTO;
    }
}
