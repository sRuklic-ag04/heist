package com.notch.heist.rest.dto;

import com.notch.heist.domain.enums.HeistStatus;
import com.notch.heist.domain.enums.MemberStatus;

import java.time.ZonedDateTime;
import java.util.List;

public class HeistDTO {

    private String name;

    private String location;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private List<HeistSkillDTO> skills;

    private HeistStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public List<HeistSkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<HeistSkillDTO> skills) {
        this.skills = skills;
    }

    public HeistStatus getStatus() {
        return status;
    }

    public void setStatus(HeistStatus status) {
        this.status = status;
    }
}
