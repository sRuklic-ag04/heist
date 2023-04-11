package com.notch.heist.repository;

import com.notch.heist.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    boolean existsByName(Object unknownAttr1);

    Optional<Skill> findByName(String skillName);
}
