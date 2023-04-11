package com.notch.heist.domain;

import java.io.Serializable;
import java.util.Objects;

public class HeistSkillKey implements Serializable {

    private Long heistId;

    private Long skillId;

    private String level;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeistSkillKey that = (HeistSkillKey) o;
        return Objects.equals(heistId, that.heistId) && Objects.equals(skillId, that.skillId) && Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heistId, skillId, level);
    }
}
