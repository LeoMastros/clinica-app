package com.unisantos.clinica_api.professional;

import com.unisantos.clinica_api.common.constants.AppConstants;
import com.unisantos.clinica_api.common.dto.ApiResponse;
import com.unisantos.clinica_api.security.user.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/professionals")
@Tag(name = "Professionals", description = "Psychologist registration and management")
public class ProfessionalController {

  private final ProfessionalService professionalService;

  public ProfessionalController(ProfessionalService professionalService) {
    this.professionalService = professionalService;
  }

  @PostMapping
  @Operation(
      summary = "Register a new psychologist",
      description =
          "Secretary or coordinator. Account is created inactive — coordinator activates it "
              + "via PATCH /api/v1/users/{id}/activation.")
  public ResponseEntity<ApiResponse<ProfessionalDto>> registerProfessional(
      @Valid @RequestBody CreateProfessionalRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    ProfessionalDto result =
        professionalService.registerProfessional(request, extractUserId(userDetails));
    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(result));
  }

  @GetMapping
  @Operation(summary = "List psychologists", description = "Secretary and coordinator only.")
  public ResponseEntity<ApiResponse<Page<ProfessionalDto>>> getProfessionals(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @AuthenticationPrincipal UserDetails userDetails) {
    Pageable pageable = PageRequest.of(page, Math.min(size, AppConstants.MAX_PAGE_SIZE));
    Page<ProfessionalDto> result =
        professionalService.getProfessionals(extractUserId(userDetails), pageable);
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  private Long extractUserId(UserDetails userDetails) {
    if (userDetails instanceof UserPrincipal userPrincipal) {
      return userPrincipal.getId();
    }
    return Long.parseLong(userDetails.getUsername());
  }
}
