package com.unisantos.clinica_api.config;

import com.unisantos.clinica_api.common.util.PasswordUtil;
import com.unisantos.clinica_api.domain.UserType;
import com.unisantos.clinica_api.domain.UserTypeRepository;
import com.unisantos.clinica_api.user.User;
import com.unisantos.clinica_api.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds the single coordinator account on first boot. There is no public signup — this is the only
 * entry point for the first admin login. The coordinator is expected to change this password
 * immediately after first login.
 *
 * <p>Credentials come from the {@code COORDINATOR_EMAIL} / {@code COORDINATOR_PASSWORD} environment
 * variables — they are intentionally not defaulted in committed configuration, so no credential is
 * exposed in the repository. If they are unset and no coordinator exists yet, seeding is skipped
 * with a warning (the app stays unadministrable until they are provided).
 */
@Component
public class DataInitializer implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);
  private static final String COORDINATOR_TYPE = "COORDINATOR";

  private final UserRepository userRepository;
  private final UserTypeRepository userTypeRepository;
  private final String coordinatorEmail;
  private final String coordinatorPassword;

  public DataInitializer(
      UserRepository userRepository,
      UserTypeRepository userTypeRepository,
      @Value("${app.bootstrap.coordinator-email}") String coordinatorEmail,
      @Value("${app.bootstrap.coordinator-password}") String coordinatorPassword) {
    this.userRepository = userRepository;
    this.userTypeRepository = userTypeRepository;
    this.coordinatorEmail = coordinatorEmail;
    this.coordinatorPassword = coordinatorPassword;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    if (userRepository.existsByUserTypeTypeName(COORDINATOR_TYPE)) {
      return;
    }
    if (coordinatorEmail == null
        || coordinatorEmail.isBlank()
        || coordinatorPassword == null
        || coordinatorPassword.isBlank()) {
      LOGGER.warn(
          "No coordinator account exists and COORDINATOR_EMAIL/COORDINATOR_PASSWORD are unset — "
              + "skipping bootstrap. Set them and restart to create the first admin account.");
      return;
    }
    UserType coordinatorType =
        userTypeRepository
            .findByTypeName(COORDINATOR_TYPE)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "User_Type COORDINATOR missing — lookup seed migration not applied"));
    userRepository.save(
        User.builder()
            .loginEmail(coordinatorEmail)
            .passwordHash(PasswordUtil.hashPassword(coordinatorPassword))
            .userType(coordinatorType)
            .isActive(true)
            .build());
    LOGGER.info("Bootstrap coordinator account created for {}", coordinatorEmail);
  }
}
