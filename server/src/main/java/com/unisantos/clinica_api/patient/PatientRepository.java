package com.unisantos.clinica_api.patient;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

  Optional<Patient> findById(Long id);

  Page<Patient> findAll(Pageable pageable);

  @Query("SELECT p FROM Patient p WHERE p.professional.user.id = :userId")
  Page<Patient> findByProfessionalUserId(@Param("userId") Long userId, Pageable pageable);

  @Query("SELECT p FROM Patient p WHERE p.professionalId = :professionalId")
  Page<Patient> findByProfessionalId(
      @Param("professionalId") Long professionalId, Pageable pageable);

  @Query("SELECT p FROM Patient p WHERE p.status.id = :statusId")
  Page<Patient> findByStatusId(@Param("statusId") Integer statusId, Pageable pageable);
}
