package com.unisantos.clinica_api.audit;

import com.unisantos.clinica_api.user.User;
import com.unisantos.clinica_api.user.UserRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

  private final AuditLogRepository auditLogRepository;
  private final UserRepository userRepository;

  public AuditService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
    this.auditLogRepository = auditLogRepository;
    this.userRepository = userRepository;
  }

  /**
   * Records a server-observable audit event. Synchronous by design (see plan's "Excluded" section).
   * Runs in the caller's transaction so the audit row is atomic with the business change it
   * describes.
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void log(String action, Long entityId, Long userId) {
    User actor = userId != null ? userRepository.getReferenceById(userId) : null;
    auditLogRepository.save(
        AuditLog.builder()
            .user(actor)
            .action(action)
            .entityName(entityNameOf(action))
            .entityId(entityId)
            .occurredAt(LocalDateTime.now())
            .build());
  }

  private String entityNameOf(String action) {
    int idx = action.indexOf('_');
    String prefix = idx > 0 ? action.substring(0, idx) : action;
    return prefix.charAt(0) + prefix.substring(1).toLowerCase();
  }
}
