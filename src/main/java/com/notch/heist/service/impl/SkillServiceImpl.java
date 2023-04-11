package com.notch.heist.service.impl;

import com.notch.heist.domain.Skill;
import com.notch.heist.repository.SkillRepository;
import com.notch.heist.service.SkillService;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill getSkill(String skillName) {
        return skillRepository.findByName(skillName)
                .orElseThrow(() -> new RuntimeException());
    }

    @Override
    public boolean skillExists(String skillName) {
        return skillRepository.existsByName(skillName);
    }

    @Override
    public Skill createSkill(String skillName) {

        return skillRepository.save(new Skill(skillName));
    }
}
