package com.unisantos.clinica_api.user;

import com.unisantos.clinica_api.domain.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JdbcTypeCode(SqlTypes.INTEGER)
  @Column(name = "id_user")
  private Long id;

  @Column(name = "first_name", columnDefinition = "VARBINARY(255)")
  private byte[] firstName;

  @Column(name = "last_name", columnDefinition = "VARBINARY(255)")
  private byte[] lastName;

  @Column(name = "login_email", length = 320, unique = true, nullable = false)
  private String loginEmail;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  // EAGER: every authenticated request needs the role (UserPrincipal, PermissionService)
  // outside any transactional context — open-in-view is disabled.
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_user_type", nullable = false)
  private UserType userType;

  @CreatedDate
  @Column(name = "registration_date", updatable = false)
  private LocalDateTime registrationDate;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;
}
