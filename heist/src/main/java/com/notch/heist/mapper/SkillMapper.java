package com.notch.heist.mapper;

import com.notch.heist.domain.MemberSkill;
import com.notch.heist.rest.dto.SkillDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SkillMapper {

    public List<SkillDTO> toDTOList(Set<MemberSkill> memberSkillSet) {
        List<SkillDTO> skillDTOList = new ArrayList<>();

        for (MemberSkill memberSkill : memberSkillSet) {
            skillDTOList.add(toDTO(memberSkill));
        }

        return skillDTOList;
    }

    public SkillDTO toDTO(MemberSkill memberSkill) {
        SkillDTO skillDTO = new SkillDTO();

        skillDTO.setLevel(memberSkill.getLevel());
        skillDTO.setName(memberSkill.getSkill().getName());

        return skillDTO;
    }
}
