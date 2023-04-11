package com.notch.heist.domain;

import jakarta.persistence.*;

@Entity
@IdClass(HeistSkillKey.class)
public class HeistSkill {

    @Id
    private Long heistId;

    @Id
    private Long skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("heistId")
    @JoinColumn(name = "heistId")
    private Heist heist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skillId")
    private Skill skill;

    @Id
    @Column(length = 10)
    private String level;

    private int membersCount;

    public Long getHeistId() {
        return heistId;
    }

    public void setHeistId(Long heistId) {
        this.heistId = heistId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public Heist getHeist() {
        return heist;
    }

    public void setHeist(Heist heist) {
        this.heist = heist;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }
}
