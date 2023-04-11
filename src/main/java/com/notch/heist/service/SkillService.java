package com.notch.heist.service;

import com.notch.heist.domain.Skill;

public interface SkillService {

    Skill getSkill(String skillName);

    boolean skillExists(String skillName);

    Skill createSkill(String skillName);
}
