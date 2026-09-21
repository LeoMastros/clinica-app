package com.unisantos.clinica_api.audit;

import com.unisantos.clinica_api.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Audit_Log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_audit")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_user", nullable = false)
  private User user;

  @Column(name = "action", length = 100, nullable = false)
  private String action;

  @Column(name = "entity_name", length = 100, nullable = false)
  private String entityName;

  @Column(name = "entity_id")
  private Long entityId;

  @Column(name = "occurred_at", nullable = false)
  private LocalDateTime occurredAt;

  @Column(name = "ip_address", columnDefinition = "VARBINARY(45)")
  private byte[] ipAddress;

  @Column(name = "user_agent", columnDefinition = "VARBINARY(255)")
  private byte[] userAgent;

  @Column(name = "details", columnDefinition = "BLOB")
  private byte[] details;
}
