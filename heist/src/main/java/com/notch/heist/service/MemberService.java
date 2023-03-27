package com.notch.heist.service;

import com.notch.heist.domain.HeistSkill;
import com.notch.heist.domain.Member;
import com.notch.heist.domain.enums.MemberStatus;
import com.notch.heist.rest.dto.HeistSkillDTO;
import com.notch.heist.rest.dto.MemberDTO;
import com.notch.heist.rest.dto.MemberSkillsDTO;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public interface MemberService {

    Member getMember(Long memberId);

    Member addMember(MemberDTO member);

    Member updateMember(Long memberId, MemberSkillsDTO memberSkillsDTO);

    void deleteSkill(Long memberId, String skillName);

    Set<MemberDTO> getMembersAvailable(ZonedDateTime startDate, ZonedDateTime endDate);

    Set<HeistSkillDTO> getHeistContributingSkills(MemberDTO memberDTO, HeistSkill heistSkill);

    Set<Member> getMembersByNames(List<String> names);

    void changeMemberStatus(Long memberId, MemberStatus memberStatus);

    void updateMembersAfterHeist(Set<Member> memberSet, int amountOfMembersToUpdate);
}
