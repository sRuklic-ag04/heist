package com.notch.heist.repository;

import com.notch.heist.domain.Member;
import com.notch.heist.domain.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    long countByName(String name);

    long countByEmail(String email);

    List<Member> findByNameIn(Collection<String> names);

    @Query(value = "SELECT * " +
            "FROM member AS m " +
            "WHERE NOT EXISTS ( " +
            "    SELECT * FROM heist AS h " +
            "            INNER JOIN heist_member AS hm on h.ID = hm.HEIST_ID " +
            "            AND ( " +
            "                ?1 BETWEEN h.START_TIME AND h.END_TIME OR " +
            "                ?2 BETWEEN h.START_TIME AND h.END_TIME OR " +
            "                h.START_TIME BETWEEN ?1 AND ?2 OR " +
            "                h.END_TIME BETWEEN ?1 AND ?2 " +
            "            ) " +
            "            WHERE hm.MEMBER_ID = m.ID " +
            ") " +
            "AND m.STATUS = 'AVAILABLE'", nativeQuery = true)
    List<Member> findAvailableMembers(ZonedDateTime startDate, ZonedDateTime endDate);
}
