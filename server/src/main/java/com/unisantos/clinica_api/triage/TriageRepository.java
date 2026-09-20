package com.unisantos.clinica_api.triage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageRepository extends JpaRepository<Triage, Integer> {}
