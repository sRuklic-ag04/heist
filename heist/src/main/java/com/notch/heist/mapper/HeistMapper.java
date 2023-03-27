package com.notch.heist.mapper;

import com.notch.heist.domain.Heist;
import com.notch.heist.rest.dto.HeistDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class HeistMapper {

    private final HeistSkillMapper heistSkillMapper;

    public HeistMapper(HeistSkillMapper heistSkillMapper) {
        this.heistSkillMapper = heistSkillMapper;
    }

    public Heist toHeist(HeistDTO heistDTO) {
        Heist heist = new Heist();
        heist.setName(heistDTO.getName());
        heist.setLocation(heistDTO.getLocation());
        heist.setStartTime(heistDTO.getStartTime());
        heist.setEndTime(heistDTO.getEndTime());
        heist.setSkills(new HashSet<>());

        return heist;
    }

    public HeistDTO toDTO(Heist heist) {
        HeistDTO heistDTO = new HeistDTO();

        heistDTO.setName(heist.getName());
        heistDTO.setLocation(heist.getLocation());
        heistDTO.setStartTime(heist.getStartTime());
        heistDTO.setEndTime(heist.getEndTime());
        heistDTO.setSkills(heistSkillMapper.toDTOList(heist.getSkills()));
        heistDTO.setStatus(heist.getStatus());

        return heistDTO;
    }
}
