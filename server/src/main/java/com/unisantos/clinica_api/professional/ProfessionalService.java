package com.unisantos.clinica_api.professional;

import com.unisantos.clinica_api.audit.AuditService;
import com.unisantos.clinica_api.common.exception.PermissionDeniedException;
import com.unisantos.clinica_api.common.exception.ValidationException;
import com.unisantos.clinica_api.common.util.PasswordUtil;
import com.unisantos.clinica_api.domain.ProfessionalLevelRepository;
import com.unisantos.clinica_api.domain.Specialty;
import com.unisantos.clinica_api.domain.SpecialtyRepository;
import com.unisantos.clinica_api.domain.UserType;
import com.unisantos.clinica_api.domain.UserTypeRepository;
import com.unisantos.clinica_api.security.permissions.PermissionService;
import com.unisantos.clinica_api.user.User;
import com.unisantos.clinica_api.user.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfessionalService {

  private static final String ROLE_PROFESSIONAL = "PROFESSIONAL";

  private final ProfessionalRepository professionalRepository;
  private final UserRepository userRepository;
  private final UserTypeRepository userTypeRepository;
  private final ProfessionalLevelRepository professionalLevelRepository;
  private final SpecialtyRepository specialtyRepository;
  private final PermissionService permissionService;
  private final AuditService auditService;

  public ProfessionalService(
      ProfessionalRepository professionalRepository,
      UserRepository userRepository,
      UserTypeRepository userTypeRepository,
      ProfessionalLevelRepository professionalLevelRepository,
      SpecialtyRepository specialtyRepository,
      PermissionService permissionService,
      AuditService auditService) {
    this.professionalRepository = professionalRepository;
    this.userRepository = userRepository;
    this.userTypeRepository = userTypeRepository;
    this.professionalLevelRepository = professionalLevelRepository;
    this.specialtyRepository = specialtyRepository;
    this.permissionService = permissionService;
    this.auditService = auditService;
  }

  /**
   * Registers a new psychologist. Secretary and Coordinator per the matrix. The account is created
   * with is_active=false — the coordinator activates it via PATCH /api/v1/users/{id}/activation
   * (which also gates login, since User.is_active controls authentication).
   */
  public ProfessionalDto registerProfessional(CreateProfessionalRequest request, Long actorId) {
    if (!permissionService.isCoordinator(actorId) && !permissionService.isSecretary(actorId)) {
      throw new PermissionDeniedException(
          "Only the coordinator or a secretary can register professionals");
    }
    if (userRepository.existsByLoginEmail(request.getEmail())) {
      throw new ValidationException("Email is already registered");
    }

    UserType professionalType =
        userTypeRepository
            .findByTypeName(ROLE_PROFESSIONAL)
            .orElseThrow(() -> new IllegalStateException("User_Type PROFESSIONAL missing"));

    User user =
        userRepository.save(
            User.builder()
                .firstName(utf8(request.getFirstName()))
                .lastName(utf8(request.getLastName()))
                .loginEmail(request.getEmail())
                .passwordHash(PasswordUtil.hashPassword(request.getPassword()))
                .userType(professionalType)
                .isActive(false)
                .build());

    Set<Specialty> specialties =
        new HashSet<>(specialtyRepository.findAllById(request.getSpecialtyIds()));
    if (specialties.size() != request.getSpecialtyIds().size()) {
      throw new ValidationException("One or more specialtyIds are invalid");
    }

    Professional professional =
        Professional.builder()
            .user(user)
            .professionalLevel(
                request.getProfessionalLevelId() != null
                    ? professionalLevelRepository
                        .findById(request.getProfessionalLevelId())
                        .orElseThrow(() -> new ValidationException("Invalid professionalLevelId"))
                    : null)
            .specialties(specialties)
            .cpf(utf8(request.getCpf()))
            .phone(utf8(request.getPhone()))
            .birthDate(utf8(request.getBirthDate()))
            .crpRegistration(utf8(request.getCrpRegistration()))
            .identificationColor(request.getIdentificationColor())
            .build();

    Professional saved = professionalRepository.save(professional);
    auditService.log("PROFESSIONAL_REGISTERED", saved.getId(), actorId);
    return toDto(saved);
  }

  /** View psychologist list — secretary (view) and coordinator (full) per matrix. */
  @Transactional(readOnly = true)
  public Page<ProfessionalDto> getProfessionals(Long actorId, Pageable pageable) {
    if (!permissionService.isCoordinator(actorId) && !permissionService.isSecretary(actorId)) {
      throw new PermissionDeniedException(
          "Only the coordinator or a secretary can view professionals");
    }
    return professionalRepository.findAll(pageable).map(this::toDto);
  }

  private ProfessionalDto toDto(Professional professional) {
    User user = professional.getUser();
    Set<Specialty> specialties = professional.getSpecialties();
    return ProfessionalDto.builder()
        .id(professional.getId())
        .userId(user != null ? user.getId() : null)
        .firstName(user != null ? str(user.getFirstName()) : null)
        .lastName(user != null ? str(user.getLastName()) : null)
        .loginEmail(user != null ? user.getLoginEmail() : null)
        .isActive(user != null ? user.getIsActive() : null)
        .professionalLevelId(
            professional.getProfessionalLevel() != null
                ? professional.getProfessionalLevel().getId()
                : null)
        .professionalLevel(
            professional.getProfessionalLevel() != null
                ? professional.getProfessionalLevel().getLevelName()
                : null)
        .specialtyIds(
            specialties == null ? List.of() : specialties.stream().map(Specialty::getId).toList())
        .specialties(
            specialties == null
                ? List.of()
                : specialties.stream().map(Specialty::getSpecialtyName).toList())
        .cpf(professional.getCpf())
        .phone(professional.getPhone())
        .birthDate(professional.getBirthDate())
        .crpRegistration(professional.getCrpRegistration())
        .identificationColor(professional.getIdentificationColor())
        .build();
  }

  private static byte[] utf8(String value) {
    return value == null ? null : value.getBytes(StandardCharsets.UTF_8);
  }

  private static String str(byte[] value) {
    return value == null ? null : new String(value, StandardCharsets.UTF_8);
  }
}
