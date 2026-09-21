package com.unisantos.clinica_api.patient;

import com.unisantos.clinica_api.common.exception.ResourceNotFoundException;
import com.unisantos.clinica_api.common.exception.ValidationException;
import com.unisantos.clinica_api.common.factory.EntityFactory;
import com.unisantos.clinica_api.domain.PatientStatusRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Factory for creating Patient entities.
 *
 * <p>Zero-Trust: does NOT encrypt/decrypt. The DTO already carries encrypted fields as
 * VARBINARY/byte[]. The factory only checks presence — it never inspects, compares, or
 * de-duplicates CPF or any other encrypted value.
 */
@Component
public class PatientFactory implements EntityFactory<Patient, PatientDto> {

  private final PatientRepository patientRepository;
  private final PatientStatusRepository patientStatusRepository;

  public PatientFactory(
      PatientRepository patientRepository, PatientStatusRepository patientStatusRepository) {
    this.patientRepository = patientRepository;
    this.patientStatusRepository = patientStatusRepository;
  }

  @Override
  public Patient create(PatientDto dto) {
    if (dto.getFirstName() == null || dto.getFirstName().length == 0) {
      throw new ValidationException("firstName is required");
    }
    if (dto.getLastName() == null || dto.getLastName().length == 0) {
      throw new ValidationException("lastName is required");
    }
    if (dto.getCpf() == null || dto.getCpf().length == 0) {
      throw new ValidationException("cpf is required");
    }

    Patient patient = new Patient();
    patient.setFirstName(dto.getFirstName());
    patient.setLastName(dto.getLastName());
    patient.setCpf(dto.getCpf());
    patient.setBirthDate(dto.getBirthDate());
    patient.setProfessionalId(dto.getProfessionalId());
    patient.setStatus(
        patientStatusRepository
            .findByName("ACTIVE")
            .orElseThrow(() -> new ResourceNotFoundException("PatientStatus ACTIVE not found")));
    patient.setRegisteredAt(LocalDateTime.now());
    return patient;
  }

  public Patient update(Long id, PatientDto dto) {
    Patient existing =
        patientRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    if (dto.getFirstName() != null) {
      existing.setFirstName(dto.getFirstName());
    }
    if (dto.getLastName() != null) {
      existing.setLastName(dto.getLastName());
    }
    if (dto.getCpf() != null) {
      existing.setCpf(dto.getCpf());
    }
    if (dto.getBirthDate() != null) {
      existing.setBirthDate(dto.getBirthDate());
    }
    return existing;
  }
}
