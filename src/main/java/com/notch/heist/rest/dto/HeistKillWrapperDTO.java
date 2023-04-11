package com.notch.heist.rest.dto;

import java.util.List;

public class HeistKillWrapperDTO {

    private List<HeistSkillDTO> skills;

    public List<HeistSkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<HeistSkillDTO> skills) {
        this.skills = skills;
    }
}
