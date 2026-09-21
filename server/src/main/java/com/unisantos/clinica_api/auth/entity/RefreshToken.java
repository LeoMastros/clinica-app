package com.unisantos.clinica_api.auth.entity;

import com.unisantos.clinica_api.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Refresh_Token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_refresh_token")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_user", nullable = false)
  private User user;

  @Column(name = "token_hash", nullable = false, unique = true, columnDefinition = "BINARY(32)")
  private byte[] tokenHash;

  @Column(name = "family_id", nullable = false)
  private Long familyId;

  @OneToOne
  @JoinColumn(name = "replaced_by_token")
  private RefreshToken replacedByToken;

  @Column(name = "issued_at", insertable = false, updatable = false)
  private LocalDateTime issuedAt;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "used_at")
  private LocalDateTime usedAt;

  @Column(name = "revoked_at")
  private LocalDateTime revokedAt;

  @Column(name = "ip_address", columnDefinition = "VARBINARY(45)")
  private byte[] ipAddress;

  @Column(name = "user_agent", columnDefinition = "VARBINARY(255)")
  private byte[] userAgent;
}
