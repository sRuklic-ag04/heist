package com.notch.heist.service;

import com.notch.heist.domain.Heist;
import com.notch.heist.domain.HeistSkill;
import com.notch.heist.domain.Member;
import com.notch.heist.rest.dto.*;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Set;

public interface HeistService {

    Heist getHeist(Long heistId);

    Heist addHeist(HeistDTO heistDTO) throws SchedulerException;

    Heist updateHeist(Long heistId, HeistKillWrapperDTO heistKillWrapperDTO);

    HeistStatusDTO getHeistStatus(Long heistId);

    Set<HeistSkill> getHeistSkills(Long heistId);

    EligibleMembersDTO getEligibleMembers(Long heistId);

    Heist confirmMembers(Long heistId, HeistMembersDTO heistMembersDTO);

    void changeHeistStatus(Long heistId, String heistStatus);

    HeistOutcomeDTO setHeistOutcome(Long heistId, Heist heist);

    HeistOutcomeDTO getHeistOutcome(Long heistId);

    void startHeist(Long heistId);

    List<Member> getHeistMembers(Long heistId);
}
