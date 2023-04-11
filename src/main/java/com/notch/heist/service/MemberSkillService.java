package com.notch.heist.service;

import com.notch.heist.rest.dto.MemberSkillsDTO;

public interface MemberSkillService {

    MemberSkillsDTO getMemberSkills(Long memberId);
}
