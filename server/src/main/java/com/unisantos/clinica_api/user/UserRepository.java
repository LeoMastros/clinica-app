package com.unisantos.clinica_api.user;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLoginEmail(String loginEmail);

  boolean existsByLoginEmail(String loginEmail);

  boolean existsByUserTypeTypeName(String typeName);

  Page<User> findByIsActive(Boolean isActive, Pageable pageable);

  Page<User> findByUserTypeTypeName(String typeName, Pageable pageable);

  Page<User> findByUserTypeTypeNameAndIsActive(
      String typeName, Boolean isActive, Pageable pageable);
}
