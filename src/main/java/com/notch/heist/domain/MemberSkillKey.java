package com.notch.heist.domain;

import java.io.Serializable;
import java.util.Objects;

public class MemberSkillKey implements Serializable {

    private Long memberId;

    private Long skillId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberSkillKey that = (MemberSkillKey) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, skillId);
    }
}
