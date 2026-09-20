package com.unisantos.clinica_api.user;

import com.unisantos.clinica_api.audit.AuditService;
import com.unisantos.clinica_api.common.exception.PermissionDeniedException;
import com.unisantos.clinica_api.common.exception.ResourceNotFoundException;
import com.unisantos.clinica_api.common.exception.ValidationException;
import com.unisantos.clinica_api.common.util.PasswordUtil;
import com.unisantos.clinica_api.domain.UserType;
import com.unisantos.clinica_api.domain.UserTypeRepository;
import com.unisantos.clinica_api.security.permissions.PermissionService;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

  private static final String ROLE_COORDINATOR = "COORDINATOR";
  private static final String ROLE_PROFESSIONAL = "PROFESSIONAL";

  private final UserRepository userRepository;
  private final UserTypeRepository userTypeRepository;
  private final PermissionService permissionService;
  private final AuditService auditService;

  public UserService(
      UserRepository userRepository,
      UserTypeRepository userTypeRepository,
      PermissionService permissionService,
      AuditService auditService) {
    this.userRepository = userRepository;
    this.userTypeRepository = userTypeRepository;
    this.permissionService = permissionService;
    this.auditService = auditService;
  }

  /**
   * Creates a SECRETARY or PROFESSIONAL account. Coordinator-only per the permissions matrix —
   * there is no public signup. Only one COORDINATOR account may ever exist; it is seeded by
   * DataInitializer and cannot be created through this endpoint.
   *
   * <p>PROFESSIONAL accounts start inactive (pending coordinator activation, per the matrix).
   * SECRETARY accounts are active immediately.
   */
  public UserDto createUser(CreateUserRequest request, Long actorId) {
    if (!permissionService.isCoordinator(actorId)) {
      throw new PermissionDeniedException("Only the coordinator can create user accounts");
    }
    if (ROLE_COORDINATOR.equals(request.getRole())) {
      throw new ValidationException("Only one coordinator account is permitted");
    }
    if (userRepository.existsByLoginEmail(request.getEmail())) {
      throw new ValidationException("Email is already registered");
    }

    UserType userType =
        userTypeRepository
            .findByTypeName(request.getRole())
            .orElseThrow(() -> new ResourceNotFoundException("Unknown role: " + request.getRole()));

    User user =
        userRepository.save(
            User.builder()
                .firstName(utf8(request.getFirstName()))
                .lastName(utf8(request.getLastName()))
                .loginEmail(request.getEmail())
                .passwordHash(PasswordUtil.hashPassword(request.getPassword()))
                .userType(userType)
                .isActive(!ROLE_PROFESSIONAL.equals(request.getRole()))
                .build());

    auditService.log("USER_CREATED", user.getId(), actorId);
    return toDto(user);
  }

  /**
   * Activates or deactivates an account. Coordinator-only. The sole coordinator account can never
   * be deactivated (would lock out admin).
   */
  public UserDto setActive(Long userId, boolean active, Long actorId) {
    if (!permissionService.isCoordinator(actorId)) {
      throw new PermissionDeniedException("Only the coordinator can activate/deactivate users");
    }
    User user = requireUser(userId);
    if (ROLE_COORDINATOR.equals(user.getUserType().getTypeName()) && !active) {
      throw new ValidationException("The coordinator account cannot be deactivated");
    }

    user.setIsActive(active);
    User saved = userRepository.save(user);
    auditService.log(active ? "USER_ACTIVATED" : "USER_DEACTIVATED", userId, actorId);
    return toDto(saved);
  }

  /** Updates editable profile fields (names, email). Coordinator-only. */
  public UserDto updateUser(Long userId, UpdateUserRequest request, Long actorId) {
    if (!permissionService.isCoordinator(actorId)) {
      throw new PermissionDeniedException("Only the coordinator can edit users");
    }
    User user = requireUser(userId);
    if (!user.getLoginEmail().equals(request.getEmail())
        && userRepository.existsByLoginEmail(request.getEmail())) {
      throw new ValidationException("Email is already registered");
    }

    user.setFirstName(utf8(request.getFirstName()));
    user.setLastName(utf8(request.getLastName()));
    user.setLoginEmail(request.getEmail());
    User saved = userRepository.save(user);
    auditService.log("USER_UPDATED", userId, actorId);
    return toDto(saved);
  }

  /** Lists user accounts — coordinator only. Optional filters: type (SECRETARY/…), active. */
  @Transactional(readOnly = true)
  public Page<UserDto> getUsers(Long actorId, String type, Boolean active, Pageable pageable) {
    if (!permissionService.isCoordinator(actorId)) {
      throw new PermissionDeniedException("Only the coordinator can list users");
    }
    if (type != null && active != null) {
      return userRepository
          .findByUserTypeTypeNameAndIsActive(type, active, pageable)
          .map(this::toDto);
    }
    if (type != null) {
      return userRepository.findByUserTypeTypeName(type, pageable).map(this::toDto);
    }
    if (active != null) {
      return userRepository.findByIsActive(active, pageable).map(this::toDto);
    }
    return userRepository.findAll(pageable).map(this::toDto);
  }

  private User requireUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
  }

  private UserDto toDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .firstName(str(user.getFirstName()))
        .lastName(str(user.getLastName()))
        .loginEmail(user.getLoginEmail())
        .userTypeId(user.getUserType() != null ? user.getUserType().getId() : null)
        .userType(user.getUserType() != null ? user.getUserType().getTypeName() : null)
        .isActive(user.getIsActive())
        .build();
  }

  private static byte[] utf8(String value) {
    return value == null ? null : value.getBytes(StandardCharsets.UTF_8);
  }

  private static String str(byte[] value) {
    return value == null ? null : new String(value, StandardCharsets.UTF_8);
  }
}
