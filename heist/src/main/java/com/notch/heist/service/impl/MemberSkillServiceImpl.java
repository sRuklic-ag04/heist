package com.notch.heist.service.impl;

import com.notch.heist.domain.MemberSkill;
import com.notch.heist.mapper.MemberSkillMapper;
import com.notch.heist.repository.MemberSkillRepository;
import com.notch.heist.rest.dto.MemberSkillsDTO;
import com.notch.heist.service.MemberSkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberSkillServiceImpl implements MemberSkillService {

    private final MemberSkillRepository memberSkillRepository;

    private final MemberSkillMapper memberSkillMapper;

    public MemberSkillServiceImpl(MemberSkillRepository memberSkillRepository, MemberSkillMapper memberSkillMapper) {
        this.memberSkillRepository = memberSkillRepository;
        this.memberSkillMapper = memberSkillMapper;
    }

    @Override
    public MemberSkillsDTO getMemberSkills(Long memberId) {
        List<MemberSkill> memberSkillList = memberSkillRepository.findByMemberId(memberId);

        return memberSkillMapper.toDTO(memberSkillList.stream().collect(Collectors.toSet()));
    }
}
