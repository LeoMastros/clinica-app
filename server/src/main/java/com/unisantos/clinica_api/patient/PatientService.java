package com.unisantos.clinica_api.patient;

import com.unisantos.clinica_api.audit.AuditService;
import com.unisantos.clinica_api.common.exception.PermissionDeniedException;
import com.unisantos.clinica_api.common.exception.ResourceNotFoundException;
import com.unisantos.clinica_api.common.exception.ValidationException;
import com.unisantos.clinica_api.domain.PatientStatus;
import com.unisantos.clinica_api.domain.PatientStatusRepository;
import com.unisantos.clinica_api.security.permissions.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientService {

  private final PatientRepository patientRepository;
  private final PatientFactory patientFactory;
  private final PatientMapper patientMapper;
  private final PermissionService permissionService;
  private final AuditService auditService;
  private final PatientStatusRepository patientStatusRepository;

  public PatientService(
      PatientRepository patientRepository,
      PatientFactory patientFactory,
      PatientMapper patientMapper,
      PermissionService permissionService,
      AuditService auditService,
      PatientStatusRepository patientStatusRepository) {
    this.patientRepository = patientRepository;
    this.patientFactory = patientFactory;
    this.patientMapper = patientMapper;
    this.permissionService = permissionService;
    this.auditService = auditService;
    this.patientStatusRepository = patientStatusRepository;
  }

  public PatientDto registerPatient(PatientDto dto, Long currentUserId) {
    if (!permissionService.canCreate("patient", currentUserId)) {
      throw new PermissionDeniedException("User does not have permission to create patients");
    }
    Patient patient = patientFactory.create(dto);
    patient.setRegisteredByUser(currentUserId);
    Patient saved = patientRepository.save(patient);
    auditService.log("PATIENT_CREATED", saved.getId(), currentUserId);
    return patientMapper.toDto(saved);
  }

  public PatientDto updatePatient(Long patientId, PatientDto dto, Long currentUserId) {
    if (!permissionService.canUpdate("patient", currentUserId, patientId)) {
      throw new PermissionDeniedException("User does not have permission to update this patient");
    }
    Patient updated = patientFactory.update(patientId, dto);
    Patient saved = patientRepository.save(updated);
    auditService.log("PATIENT_UPDATED", patientId, currentUserId);
    return patientMapper.toDto(saved);
  }

  /**
   * Updates only the patient's status (ACTIVE, DEACTIVATED, DISCHARGED). Does NOT touch any other
   * field — implemented separately from updatePatient() so it never overwrites encrypted fields
   * with nulls.
   */
  public PatientDto updateStatus(Long patientId, String statusName, Long currentUserId) {
    if (!permissionService.canUpdate("patient", currentUserId, patientId)) {
      throw new PermissionDeniedException(
          "User does not have permission to update this patient's status");
    }
    Patient patient =
        patientRepository
            .findById(patientId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    PatientStatus status =
        patientStatusRepository
            .findByName(statusName)
            .orElseThrow(() -> new ValidationException("Invalid status: " + statusName));
    patient.setStatus(status);
    Patient saved = patientRepository.save(patient);
    auditService.log("PATIENT_STATUS_UPDATED", patientId, currentUserId);
    return patientMapper.toDto(saved);
  }

  /**
   * Assigns or changes the responsible psychologist. Coordinator-only — enforced via
   * permissionService, not just the controller layer. Does NOT touch any other field. Re-enveloping
   * the patient's encrypted DEK for the new psychologist's public key is a frontend responsibility
   * triggered after this call succeeds (see "Excluded" section).
   */
  public PatientDto assignPsychologist(Long patientId, Long professionalId, Long currentUserId) {
    if (!permissionService.isCoordinator(currentUserId)) {
      throw new PermissionDeniedException("Only coordinators can assign a psychologist");
    }
    Patient patient =
        patientRepository
            .findById(patientId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    patient.setProfessionalId(professionalId);
    Patient saved = patientRepository.save(patient);
    auditService.log("PATIENT_PSYCHOLOGIST_ASSIGNED", patientId, currentUserId);
    return patientMapper.toDto(saved);
  }

  public PatientDto getPatientById(Long patientId, Long currentUserId) {
    if (!permissionService.canRead("patient", currentUserId, patientId)) {
      throw new PermissionDeniedException("User does not have permission to view this patient");
    }
    Patient patient =
        patientRepository
            .findById(patientId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    return patientMapper.toDto(patient);
  }

  public Page<PatientDto> getAllPatients(Long currentUserId, Pageable pageable) {
    Page<Patient> patients =
        permissionService.isPsychologist(currentUserId)
            ? patientRepository.findByProfessionalUserId(currentUserId, pageable)
            : patientRepository.findAll(pageable);
    return patients.map(patientMapper::toDto);
  }
}
