package com.unisantos.clinica_api.security.permissions;

import com.unisantos.clinica_api.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private static final String ROLE_COORDINATOR = "COORDINATOR";
  private static final String ROLE_SECRETARY = "SECRETARY";
  private static final String ROLE_PSYCHOLOGIST = "PROFESSIONAL";

  private static final PermissionStrategy DENY_ALL =
      new PermissionStrategy() {
        @Override
        public boolean canCreate(String resource, Long userId) {
          return false;
        }

        @Override
        public boolean canRead(String resource, Long userId, Long resourceId) {
          return false;
        }

        @Override
        public boolean canUpdate(String resource, Long userId, Long resourceId) {
          return false;
        }

        @Override
        public boolean canDelete(String resource, Long userId, Long resourceId) {
          return false;
        }

        @Override
        public boolean isCoordinator(Long userId) {
          return false;
        }

        @Override
        public boolean isSecretary(Long userId) {
          return false;
        }

        @Override
        public boolean isPsychologist(Long userId) {
          return false;
        }
      };

  private final UserRepository userRepository;
  private final CoordinatorPermissionStrategy coordinatorStrategy;
  private final SecretaryPermissionStrategy secretaryStrategy;
  private final PsychologistPermissionStrategy psychologistStrategy;

  public PermissionService(
      UserRepository userRepository,
      CoordinatorPermissionStrategy coordinatorStrategy,
      SecretaryPermissionStrategy secretaryStrategy,
      PsychologistPermissionStrategy psychologistStrategy) {
    this.userRepository = userRepository;
    this.coordinatorStrategy = coordinatorStrategy;
    this.secretaryStrategy = secretaryStrategy;
    this.psychologistStrategy = psychologistStrategy;
  }

  public boolean canCreate(String resource, Long userId) {
    return strategyFor(userId).canCreate(resource, userId);
  }

  public boolean canRead(String resource, Long userId, Long entityId) {
    return strategyFor(userId).canRead(resource, userId, entityId);
  }

  public boolean canUpdate(String resource, Long userId, Long entityId) {
    return strategyFor(userId).canUpdate(resource, userId, entityId);
  }

  public boolean canDelete(String resource, Long userId, Long entityId) {
    return strategyFor(userId).canDelete(resource, userId, entityId);
  }

  public boolean isCoordinator(Long userId) {
    return ROLE_COORDINATOR.equals(roleOf(userId));
  }

  public boolean isSecretary(Long userId) {
    return ROLE_SECRETARY.equals(roleOf(userId));
  }

  public boolean isPsychologist(Long userId) {
    return ROLE_PSYCHOLOGIST.equals(roleOf(userId));
  }

  private PermissionStrategy strategyFor(Long userId) {
    return switch (roleOf(userId)) {
      case ROLE_COORDINATOR -> coordinatorStrategy;
      case ROLE_SECRETARY -> secretaryStrategy;
      case ROLE_PSYCHOLOGIST -> psychologistStrategy;
      default -> DENY_ALL;
    };
  }

  private String roleOf(Long userId) {
    if (userId == null) {
      return "";
    }
    return userRepository
        .findById(userId)
        .map(u -> u.getUserType() != null ? u.getUserType().getTypeName() : "")
        .orElse("");
  }
}
