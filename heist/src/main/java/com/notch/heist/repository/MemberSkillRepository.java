package com.notch.heist.repository;

import com.notch.heist.domain.MemberSkill;
import com.notch.heist.domain.MemberSkillKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSkillRepository extends JpaRepository<MemberSkill, MemberSkillKey> {
    List<MemberSkill> findByMemberId(Long memberId);
    long deleteByMemberIdAndSkillId(Long memberId, Long skillId);

}
