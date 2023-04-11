package com.notch.heist.mapper;

import com.notch.heist.domain.MemberSkill;
import com.notch.heist.rest.dto.MemberSkillsDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MemberSkillMapper {

    private final SkillMapper skillMapper;

    public MemberSkillMapper(SkillMapper skillMapper) {
        this.skillMapper = skillMapper;
    }

    public MemberSkillsDTO toDTO(Set<MemberSkill> memberSkillSet) {
        MemberSkillsDTO memberSkillsDTO = new MemberSkillsDTO();

        memberSkillsDTO.setMainSkill("pom");
        memberSkillsDTO.setSkills(skillMapper.toDTOList(memberSkillSet));

        return memberSkillsDTO;
    }
}
