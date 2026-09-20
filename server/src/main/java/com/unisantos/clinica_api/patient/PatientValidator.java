package com.unisantos.clinica_api.patient;

import com.unisantos.clinica_api.common.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PatientValidator {

  public void validateForCreation(PatientDto dto) {
    if (dto.getFirstName() == null || dto.getFirstName().length == 0) {
      throw new ValidationException("First name is required");
    }
    if (dto.getLastName() == null || dto.getLastName().length == 0) {
      throw new ValidationException("Last name is required");
    }
    if (dto.getProfessionalId() == null) {
      throw new ValidationException("Professional ID is required");
    }
    if (dto.getPatientStatusId() == null) {
      throw new ValidationException("Patient status ID is required");
    }
  }

  public void validateForUpdate(PatientDto dto) {
    if (dto.getId() == null) {
      throw new ValidationException("Patient ID is required for update");
    }
    validateForCreation(dto);
  }
}
