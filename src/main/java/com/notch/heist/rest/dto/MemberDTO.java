package com.notch.heist.rest.dto;

import com.notch.heist.domain.enums.Sex;
import com.notch.heist.domain.enums.MemberStatus;

import java.util.List;

public class MemberDTO {

    private String name;

    private Sex sex;

    private String email;

    private List<SkillDTO> skills;

    private String mainSkill;

    private MemberStatus memberStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public MemberStatus getStatus() {
        return memberStatus;
    }

    public void setStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }
}
