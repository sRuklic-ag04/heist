package com.notch.heist.repository;

import com.notch.heist.domain.Heist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeistRepository extends JpaRepository<Heist, Long> {
    long countByName(String name);

}
