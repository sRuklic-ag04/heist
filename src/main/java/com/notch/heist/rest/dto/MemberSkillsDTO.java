package com.notch.heist.rest.dto;

import java.util.List;

public class MemberSkillsDTO {

    private List<SkillDTO> skills;
    private String mainSkill;

    public List<SkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDTO> skills) {
        this.skills = skills;
    }

    public String getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(String mainSkill) {
        this.mainSkill = mainSkill;
    }
}
