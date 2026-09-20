package com.unisantos.clinica_api.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.unisantos.clinica_api.audit.AuditService;
import com.unisantos.clinica_api.common.exception.PermissionDeniedException;
import com.unisantos.clinica_api.domain.PatientStatus;
import com.unisantos.clinica_api.domain.PatientStatusRepository;
import com.unisantos.clinica_api.security.permissions.PermissionService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Reference test class — the template other developers copy when they implement tests for their own
 * Tier 3 feature.
 *
 * <p>Test scenarios based on business rules: 1. Secretary can register patients (permission check)
 * 2. Coordinator can register patients (permission check) 3. Psychologist cannot register patients
 * (permission denied) 4. CPF bytes are stored and returned as-is — never validated, compared, or
 * checked for uniqueness server-side (opaque encrypted VARBINARY) 5. updateStatus() only changes
 * status, leaves other fields untouched 6. assignPsychologist() is rejected for non-coordinator
 * roles
 *
 * <p>Reference: Permissions Matrix section 2.3 Reference: Database schema - Patient table
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

  @Mock private PatientRepository patientRepository;
  @Mock private PatientFactory patientFactory;
  @Mock private PatientMapper patientMapper;
  @Mock private PermissionService permissionService;
  @Mock private AuditService auditService;
  @Mock private PatientStatusRepository patientStatusRepository;

  @InjectMocks private PatientService patientService;

  @Test
  @DisplayName("Scenario 1: Secretary can register patients (permission check granted)")
  void secretaryCanRegisterPatient() {
    Long secretaryId = 10L;
    PatientDto dto =
        PatientDto.builder().firstName("John".getBytes()).lastName("Doe".getBytes()).build();
    Patient patient = new Patient();
    patient.setId(1L);
    PatientDto expectedDto = PatientDto.builder().id(1L).build();

    when(permissionService.canCreate("patient", secretaryId)).thenReturn(true);
    when(patientFactory.create(dto)).thenReturn(patient);
    when(patientRepository.save(patient)).thenReturn(patient);
    when(patientMapper.toDto(patient)).thenReturn(expectedDto);

    PatientDto result = patientService.registerPatient(dto, secretaryId);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(auditService).log("PATIENT_CREATED", 1L, secretaryId);
  }

  @Test
  @DisplayName("Scenario 2: Coordinator can register patients (permission check granted)")
  void coordinatorCanRegisterPatient() {
    Long coordinatorId = 1L;
    PatientDto dto =
        PatientDto.builder().firstName("Jane".getBytes()).lastName("Doe".getBytes()).build();
    Patient patient = new Patient();
    patient.setId(2L);
    PatientDto expectedDto = PatientDto.builder().id(2L).build();

    when(permissionService.canCreate("patient", coordinatorId)).thenReturn(true);
    when(patientFactory.create(dto)).thenReturn(patient);
    when(patientRepository.save(patient)).thenReturn(patient);
    when(patientMapper.toDto(patient)).thenReturn(expectedDto);

    PatientDto result = patientService.registerPatient(dto, coordinatorId);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(2L);
    verify(auditService).log("PATIENT_CREATED", 2L, coordinatorId);
  }

  @Test
  @DisplayName("Scenario 3: Psychologist cannot register patients (permission denied)")
  void psychologistCannotRegisterPatient() {
    Long psychologistId = 5L;
    PatientDto dto = PatientDto.builder().firstName("Forbidden".getBytes()).build();

    when(permissionService.canCreate("patient", psychologistId)).thenReturn(false);

    assertThatThrownBy(() -> patientService.registerPatient(dto, psychologistId))
        .isInstanceOf(PermissionDeniedException.class)
        .hasMessageContaining("User does not have permission to create patients");
  }

  @Test
  @DisplayName("Scenario 4: CPF bytes are stored and returned as-is (opaque encrypted VARBINARY)")
  void cpfBytesStoredAndReturnedAsIs() {
    Long secretaryId = 10L;
    byte[] encryptedCpf = new byte[] {0x12, (byte) 0xAB, 0x34, (byte) 0xCD};
    PatientDto dto = PatientDto.builder().cpf(encryptedCpf).build();

    Patient patient = new Patient();
    patient.setId(3L);
    patient.setCpf(encryptedCpf);

    PatientDto mappedDto = PatientDto.builder().id(3L).cpf(encryptedCpf).build();

    when(permissionService.canCreate("patient", secretaryId)).thenReturn(true);
    when(patientFactory.create(dto)).thenReturn(patient);
    when(patientRepository.save(patient)).thenReturn(patient);
    when(patientMapper.toDto(patient)).thenReturn(mappedDto);

    PatientDto result = patientService.registerPatient(dto, secretaryId);

    assertThat(result.getCpf()).isEqualTo(encryptedCpf);
  }

  @Test
  @DisplayName("Scenario 5: updateStatus() only changes status, leaves other fields untouched")
  void updateStatusChangesOnlyStatus() {
    Long userId = 1L;
    Long patientId = 100L;
    byte[] originalFirstName = "OriginalFirstName".getBytes();

    Patient existingPatient = new Patient();
    existingPatient.setId(patientId);
    existingPatient.setFirstName(originalFirstName);

    PatientStatus activeStatus = PatientStatus.builder().id(1).statusName("ACTIVE").build();
    PatientStatus deactivatedStatus =
        PatientStatus.builder().id(2).statusName("DEACTIVATED").build();

    existingPatient.setStatus(activeStatus);

    when(permissionService.canUpdate("patient", userId, patientId)).thenReturn(true);
    when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
    when(patientStatusRepository.findByName("DEACTIVATED"))
        .thenReturn(Optional.of(deactivatedStatus));
    when(patientRepository.save(any(Patient.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    PatientDto resultDto = PatientDto.builder().id(patientId).firstName(originalFirstName).build();
    when(patientMapper.toDto(any(Patient.class))).thenReturn(resultDto);

    PatientDto result = patientService.updateStatus(patientId, "DEACTIVATED", userId);

    assertThat(existingPatient.getStatus()).isEqualTo(deactivatedStatus);
    assertThat(existingPatient.getFirstName()).isEqualTo(originalFirstName);
    verify(auditService).log("PATIENT_STATUS_UPDATED", patientId, userId);
  }

  @Test
  @DisplayName("Scenario 6: assignPsychologist() is rejected for non-coordinator roles")
  void assignPsychologistRejectedForNonCoordinator() {
    Long secretaryId = 10L;
    Long patientId = 100L;
    Long professionalId = 7L;

    when(permissionService.isCoordinator(secretaryId)).thenReturn(false);

    assertThatThrownBy(
            () -> patientService.assignPsychologist(patientId, professionalId, secretaryId))
        .isInstanceOf(PermissionDeniedException.class)
        .hasMessageContaining("Only coordinators can assign a psychologist");
  }
}
