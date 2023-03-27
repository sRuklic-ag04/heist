package com.notch.heist.mapper;

import com.notch.heist.domain.HeistSkill;
import com.notch.heist.rest.dto.HeistSkillDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class HeistSkillMapper {

    public HeistSkillDTO toDTO(HeistSkill heistSkill) {
        HeistSkillDTO heistSkillDTO = new HeistSkillDTO();

        heistSkillDTO.setName(heistSkill.getHeist().getName());
        heistSkillDTO.setLevel(heistSkill.getLevel());
        heistSkillDTO.setMembers(heistSkill.getMembersCount());

        return heistSkillDTO;
    }

    public List<HeistSkillDTO> toDTOList(Set<HeistSkill> heistSkillSet) {
        List<HeistSkillDTO> heistSkillDTOList = new ArrayList<>();

        for (HeistSkill heistSkill : heistSkillSet) {
            heistSkillDTOList.add(toDTO(heistSkill));
        }

        return heistSkillDTOList;
    }
}
