package com.notch.heist.rest.dto;

import java.util.List;

public class EligibleMembersDTO {

    private List<HeistSkillDTO> skills;
    private List<MemberDTO> members;

    public List<HeistSkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<HeistSkillDTO> skills) {
        this.skills = skills;
    }

    public List<MemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDTO> members) {
        this.members = members;
    }
}
