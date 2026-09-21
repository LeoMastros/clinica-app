package com.unisantos.clinica_api.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientStatusRepository extends JpaRepository<PatientStatus, Integer> {

  Optional<PatientStatus> findByStatusName(String statusName);

  default Optional<PatientStatus> findByName(String name) {
    return findByStatusName(name);
  }
}
