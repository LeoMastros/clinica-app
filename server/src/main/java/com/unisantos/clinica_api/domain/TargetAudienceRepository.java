package com.unisantos.clinica_api.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetAudienceRepository extends JpaRepository<TargetAudience, Integer> {}
