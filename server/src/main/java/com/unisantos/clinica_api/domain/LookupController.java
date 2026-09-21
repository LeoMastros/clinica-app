package com.unisantos.clinica_api.domain;

import com.unisantos.clinica_api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Read-only lookup tables used by forms (any authenticated user). */
@RestController
@RequestMapping("/api/v1/lookups")
@Tag(name = "Lookups", description = "Reference data for forms")
public class LookupController {

  public record LookupItem(Integer id, String name) {}

  private final SpecialtyRepository specialtyRepository;
  private final ProfessionalLevelRepository professionalLevelRepository;

  public LookupController(
      SpecialtyRepository specialtyRepository,
      ProfessionalLevelRepository professionalLevelRepository) {
    this.specialtyRepository = specialtyRepository;
    this.professionalLevelRepository = professionalLevelRepository;
  }

  @GetMapping("/specialties")
  @Operation(summary = "List specialties")
  public ResponseEntity<ApiResponse<List<LookupItem>>> getSpecialties() {
    List<LookupItem> items =
        specialtyRepository.findAll().stream()
            .map(s -> new LookupItem(s.getId(), s.getSpecialtyName()))
            .toList();
    return ResponseEntity.ok(new ApiResponse<>(items));
  }

  @GetMapping("/professional-levels")
  @Operation(summary = "List professional levels")
  public ResponseEntity<ApiResponse<List<LookupItem>>> getProfessionalLevels() {
    List<LookupItem> items =
        professionalLevelRepository.findAll().stream()
            .map(l -> new LookupItem(l.getId(), l.getLevelName()))
            .toList();
    return ResponseEntity.ok(new ApiResponse<>(items));
  }
}
