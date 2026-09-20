package com.unisantos.clinica_api.user;

import com.unisantos.clinica_api.common.constants.AppConstants;
import com.unisantos.clinica_api.common.dto.ApiResponse;
import com.unisantos.clinica_api.common.exception.ValidationException;
import com.unisantos.clinica_api.security.user.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User account management — coordinator only")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(
      summary = "List user accounts",
      description =
          "Coordinator only. Optional filters: type=COORDINATOR|SECRETARY|PROFESSIONAL, "
              + "active=true|false.")
  public ResponseEntity<ApiResponse<Page<UserDto>>> getUsers(
      @RequestParam(required = false) String type,
      @RequestParam(required = false) Boolean active,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @AuthenticationPrincipal UserDetails userDetails) {
    Pageable pageable = PageRequest.of(page, Math.min(size, AppConstants.MAX_PAGE_SIZE));
    Page<UserDto> result = userService.getUsers(extractUserId(userDetails), type, active, pageable);
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  @PostMapping
  @Operation(
      summary = "Create a user account",
      description = "Coordinator only. Creates a SECRETARY or PROFESSIONAL account.")
  public ResponseEntity<ApiResponse<UserDto>> createUser(
      @Valid @RequestBody CreateUserRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    UserDto result = userService.createUser(request, extractUserId(userDetails));
    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(result));
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Edit a user",
      description = "Coordinator only. Updates name and e-mail — not role or password.")
  public ResponseEntity<ApiResponse<UserDto>> updateUser(
      @PathVariable Long id,
      @Valid @RequestBody UpdateUserRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    UserDto result = userService.updateUser(id, request, extractUserId(userDetails));
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  /** PATCH /api/v1/users/{id}/activation — body: {"active": true|false} — COORDINATOR ONLY */
  @PatchMapping("/{id}/activation")
  @Operation(
      summary = "Activate or deactivate a user",
      description = "Coordinator only. Professionals cannot authenticate until activated.")
  public ResponseEntity<ApiResponse<UserDto>> setActive(
      @PathVariable Long id,
      @RequestBody Map<String, Boolean> body,
      @AuthenticationPrincipal UserDetails userDetails) {
    Boolean active = body.get("active");
    if (active == null) {
      throw new ValidationException("active is required");
    }
    UserDto result = userService.setActive(id, active, extractUserId(userDetails));
    return ResponseEntity.ok(new ApiResponse<>(result));
  }

  private Long extractUserId(UserDetails userDetails) {
    if (userDetails instanceof UserPrincipal userPrincipal) {
      return userPrincipal.getId();
    }
    return Long.parseLong(userDetails.getUsername());
  }
}
