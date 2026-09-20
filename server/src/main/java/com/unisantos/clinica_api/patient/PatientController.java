package com.unisantos.clinica_api.patient;

import com.unisantos.clinica_api.common.constants.AppConstants;
import com.unisantos.clinica_api.common.dto.ApiResponse;
import com.unisantos.clinica_api.common.exception.ValidationException;
import com.unisantos.clinica_api.security.user.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Patient management API")
public class PatientController {

  private final PatientService patientService;

  @Operation(summary = "Register a new patient")
  @PostMapping
  public ResponseEntity<ApiResponse<PatientDto>> registerPatient(
      @Valid @RequestBody PatientDto dto, @AuthenticationPrincipal UserDetails userDetails) {
    Long currentUserId = extractUserId(userDetails);
    PatientDto result = patientService.registerPatient(dto, currentUserId);
    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(result));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<PatientDto>> getPatient(
      @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    PatientDto result = patientService.getPatientById(id, extractUserId(userDetails));
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<Page<PatientDto>>> getAllPatients(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "lastName") String sortBy,
      @AuthenticationPrincipal UserDetails userDetails) {
    Pageable pageable =
        PageRequest.of(page, Math.min(size, AppConstants.MAX_PAGE_SIZE), Sort.by(sortBy));
    Page<PatientDto> result = patientService.getAllPatients(extractUserId(userDetails), pageable);
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<PatientDto>> updatePatient(
      @PathVariable Long id,
      @Valid @RequestBody PatientDto dto,
      @AuthenticationPrincipal UserDetails userDetails) {
    PatientDto result = patientService.updatePatient(id, dto, extractUserId(userDetails));
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  /** PATCH /api/v1/patients/{id}/status — body: {"status": "DEACTIVATED"} */
  @PatchMapping("/{id}/status")
  public ResponseEntity<ApiResponse<PatientDto>> updatePatientStatus(
      @PathVariable Long id,
      @RequestBody Map<String, String> statusUpdate,
      @AuthenticationPrincipal UserDetails userDetails) {
    String status = statusUpdate.get("status");
    if (status == null || status.isBlank()) {
      throw new ValidationException("status is required");
    }
    PatientDto result = patientService.updateStatus(id, status, extractUserId(userDetails));
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  /** PATCH /api/v1/patients/{id}/psychologist — body: {"professionalId": 7} — COORDINATOR ONLY */
  @PatchMapping("/{id}/psychologist")
  public ResponseEntity<ApiResponse<PatientDto>> assignPsychologist(
      @PathVariable Long id,
      @RequestBody Map<String, Long> assignment,
      @AuthenticationPrincipal UserDetails userDetails) {
    Long professionalId = assignment.get("professionalId");
    if (professionalId == null) {
      throw new ValidationException("professionalId is required");
    }
    PatientDto result =
        patientService.assignPsychologist(id, professionalId, extractUserId(userDetails));
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  private Long extractUserId(UserDetails userDetails) {
    if (userDetails instanceof UserPrincipal userPrincipal) {
      return userPrincipal.getId();
    }
    return Long.parseLong(userDetails.getUsername());
  }
}
