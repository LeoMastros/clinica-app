# Spring Boot Backend Structure Plan (Feature-Based Architecture) — Final

**Purpose:** a consistent, correctly-patterned code *structure* for the whole team to build into — not a fully working product, except where noted (Tier 1 and Tier 2 below). Build real code only where inconsistency between developers would cause conflicts or rework; everywhere else, generate the minimal class shell and stop.

**Source-of-truth files (read these, don't re-derive them):**
- Database schema: `/server/schema.dbml`
- Permissions matrix: `/server/permissions-matrix.md`

Do not invent table columns, relationships, or role/permission rules. If something needed isn't in these two files, stop and flag it instead of guessing.

**Target database: MySQL.** Encrypted fields use MySQL's `VARBINARY`/`BLOB` types, mapped to `byte[]` entity fields via standard JPA. Use this consistently across all entities and `V1__init_schema.sql`.

---

## Tier Classification

- **Tier 1 (Fully Functional)** — shared contracts every feature depends on. Built for real.
- **Tier 2 (Reference Implementation)** — the `patient` feature, built fully as the template every other feature copies.
- **Tier 3 (Empty Shells)** — every other feature. Class definitions only, no method bodies, no comments beyond a one-line pointer to the Tier 2 example. Minimizes tokens.

---

## Code Quality and Formatting Strategy

### Deep Package Structure Strategy

- **No wildcard imports for project packages.** Explicit imports only (no `import com.unisantos.clinica_api.patient.*;`). Wildcard imports allowed only for standard Java libraries (`java.io`, `java.net`, `java.util`, `org.junit`), enforced by Checkstyle.
- **Static imports for utility methods**, e.g. `import static com.unisantos.clinica_api.common.util.JwtUtil.*;` — for validation utilities, assertion libraries (AssertJ), and common helpers.
- **Lombok** (`@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`) to reduce getter/setter/constructor boilerplate.
- **Package-level organization**: each feature is self-contained (Controller, Service, Repository, Entity, DTO in the same package), minimizing cross-package imports.
- **IDE configuration**: organize imports automatically on save (Java standard libraries → third-party → project packages), remove unused imports on save.

### Spotless Configuration (build.gradle)

```gradle
spotless {
    java {
        googleJavaFormat()  // Owns indentation (2 spaces) — do not also set indentWithSpaces()
        target 'src/main/java/**/*.java', 'src/test/java/**/*.java'
        trimTrailingWhitespace()
        endWithNewline()
        removeUnusedImports()
        importOrder()
    }
}
```

### Checkstyle Configuration (checkstyle.xml)

```xml
<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
    "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
    "https://checkstyle.org/dtds/configuration_1_3.dtd">

<module name="Checker">
    <property name="charset" value="UTF-8"/>
    <property name="severity" value="warning"/>

    <module name="FileLength">
        <property name="max" value="2000"/>
    </module>

    <module name="LineLength">
        <property name="max" value="120"/>
        <property name="ignorePattern" value="^package.*|^import.*|a href|href|http://|https://|ftp://"/>
    </module>

    <!-- Suppression filter for Tier 3 empty class shells -->
    <module name="SuppressionFilter">
        <property name="file" value="config/checkstyle/suppressions.xml"/>
    </module>

    <module name="TreeWalker">
        <module name="ConstantName"/>
        <module name="LocalFinalVariableName"/>
        <module name="LocalVariableName"/>
        <module name="MemberName"/>
        <module name="MethodName"/>
        <module name="PackageName"/>
        <module name="ParameterName"/>
        <module name="StaticVariableName"/>
        <module name="TypeName"/>

        <module name="AvoidStarImport">
            <property name="allowClassStarImport" value="false"/>
            <property name="excludes" value="java.io,java.net,java.util,org.junit"/>
        </module>
        <module name="UnusedImports"/>
        <module name="RedundantImport"/>
        <module name="IllegalImport"/>

        <module name="EmptyForIteratorPad"/>
        <module name="GenericWhitespace"/>
        <module name="MethodParamPad"/>
        <module name="NoWhitespaceAfter"/>
        <module name="NoWhitespaceBefore"/>
        <module name="OperatorWrap"/>
        <module name="ParenPad"/>
        <module name="TypecastParenPad"/>
        <module name="WhitespaceAfter"/>
        <module name="WhitespaceAround"/>

        <module name="ModifierOrder"/>
        <module name="RedundantModifier"/>

        <module name="AvoidNestedBlocks"/>
        <module name="EmptyBlock"/>
        <module name="LeftCurly"/>
        <module name="NeedBraces"/>
        <module name="RightCurly"/>

        <module name="EmptyStatement"/>
        <module name="EqualsHashCode"/>
        <module name="HiddenField">
            <property name="ignoreConstructorParameter" value="true"/>
            <property name="ignoreSetter" value="true"/>
        </module>
        <module name="IllegalInstantiation"/>
        <module name="InnerAssignment"/>
        <module name="MagicNumber">
            <property name="ignoreNumbers" value="-1, 0, 1, 2"/>
        </module>
        <module name="MissingSwitchDefault"/>
        <module name="SimplifyBooleanExpression"/>
        <module name="SimplifyBooleanReturn"/>

        <module name="AnnotationLocation">
            <property name="allowSamelineMultipleAnnotations" value="false"/>
            <property name="allowSamelineSingleParameterlessAnnotation" value="false"/>
            <property name="allowSamelineParameterizedAnnotation" value="false"/>
        </module>

        <module name="JavadocMethod">
            <property name="scope" value="public"/>
            <property name="allowMissingParamTags" value="false"/>
            <property name="allowMissingReturnTag" value="false"/>
        </module>
        <module name="JavadocType">
            <property name="scope" value="public"/>
        </module>
        <module name="JavadocVariable">
            <property name="scope" value="public"/>
        </module>
    </module>
</module>
```

### Checkstyle Suppression Filter (config/checkstyle/suppressions.xml)

```xml
<?xml version="1.0"?>
<!DOCTYPE suppressions PUBLIC
    "-//Checkstyle//DTD SuppressionFilter Configuration 1.2//EN"
    "https://checkstyle.org/dtds/suppressions_1_2.dtd">

<suppressions>
    <!-- Tier 3 packages contain only empty class shells with no method bodies. -->
    <!-- Remove a package's entry here once that feature is actually implemented. -->
    <suppress checks="JavadocMethod" files="(professional|appointment|session|triage|anamnesis|report|cryptography|audit|user)/.*\.java"/>
    <suppress checks="JavadocVariable" files="(professional|appointment|session|triage|anamnesis|report|cryptography|audit|user)/.*\.java"/>
</suppressions>
```

### Gradle Integration

```gradle
plugins {
    id 'com.diffplug.spotless' version '6.25.0'
    id 'checkstyle'
}

checkstyle {
    toolVersion = '10.12.5'
    configFile = file('config/checkstyle/checkstyle.xml')
    ignoreFailures = false
    maxWarnings = 0
}

tasks.withType(Checkstyle) {
    reports {
        xml.required = true
        html.required = true
    }
}

build.dependsOn 'spotlessCheck'
```

---

## Design Patterns Selection

### Creational Pattern: Factory Method
Entity creation (Patient now, other features later) with presence validation, separating creation logic from business logic.

### Structural Pattern: Strategy
Permission checking per role (Coordinator, Secretary, Psychologist) — swap algorithms without changing client code.

### Why Other Patterns Were Not Used
- **Builder**: entities don't have complex construction with optional parameters; Factory + DTO is sufficient.
- **Singleton**: Spring's `@Component`/DI already manages bean singletons.
- **Adapter**: no incompatible interfaces to bridge.
- **Decorator**: permission checks live in the service layer, not via object decoration.
- **Observer**: no event-driven communication required; audit logging is synchronous in service methods (see "Excluded" section for the open question on this).
- **Template Method**: service methods don't share varying algorithmic steps.
- **Chain of Responsibility**: permission checking is a single responsibility (Strategy), not a handler chain.
- **Facade**: each feature's Service class already acts as a facade for its domain.
- **Proxy**: Spring's `@Transactional` already provides proxying.
- **Composite**: no tree-like object structures.

---

## Complete Folder Structure (Feature-Based Architecture with Tier Classification)

```
src/main/java/com/unisantos/clinica_api/
├── ClinicaApiApplication.java

├── config/                              # TIER 1 - FULLY FUNCTIONAL
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   ├── JpaConfig.java
│   ├── SwaggerConfig.java
│   └── FlywayConfig.java

├── common/                              # TIER 1 - FULLY FUNCTIONAL
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── PermissionDeniedException.java
│   │   └── ValidationException.java
│   ├── dto/
│   │   ├── ApiResponse.java
│   │   ├── PageResponse.java
│   │   └── ErrorResponse.java
│   ├── util/
│   │   ├── JwtUtil.java
│   │   └── PasswordUtil.java
│   ├── factory/
│   │   ├── EntityFactory.java           # interface only
│   │   └── EntityFactoryProvider.java
│   └── constants/
│       ├── SecurityConstants.java
│       └── AppConstants.java

├── security/                            # TIER 1 - FULLY FUNCTIONAL
│   ├── jwt/
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtTokenProvider.java
│   ├── user/
│   │   └── CustomUserDetailsService.java
│   └── permissions/
│       ├── PermissionService.java
│       ├── PermissionStrategy.java
│       ├── CoordinatorPermissionStrategy.java   # built from permissions-matrix.md
│       ├── SecretaryPermissionStrategy.java     # built from permissions-matrix.md
│       ├── PsychologistPermissionStrategy.java  # built from permissions-matrix.md
│       └── PermissionContext.java

├── auth/                                # TIER 1 - FULLY FUNCTIONAL
│   ├── AuthController.java
│   ├── AuthService.java
│   └── AuthDto.java

├── patient/                             # TIER 2 - REFERENCE IMPLEMENTATION (all FULLY FUNCTIONAL)
│   ├── PatientController.java
│   ├── PatientService.java
│   ├── PatientRepository.java
│   ├── Patient.java
│   ├── PatientAddress.java
│   ├── PatientGuardian.java
│   ├── EmergencyContact.java
│   ├── PatientTag.java
│   ├── PatientDto.java
│   ├── PatientFactory.java
│   ├── PatientMapper.java
│   └── PatientValidator.java

├── user/                                # TIER 3 - EMPTY SHELLS
│   ├── UserController.java
│   ├── UserService.java
│   ├── UserRepository.java
│   ├── User.java
│   ├── UserDto.java
│   ├── UserFactory.java
│   ├── UserMapper.java
│   └── UserValidator.java

├── professional/                        # TIER 3 - EMPTY SHELLS
│   ├── ProfessionalController.java
│   ├── ProfessionalService.java
│   ├── ProfessionalRepository.java
│   ├── Professional.java
│   ├── ProfessionalTargetAudience.java
│   ├── ProfessionalAvailability.java
│   ├── ProfessionalDto.java
│   ├── ProfessionalFactory.java
│   ├── ProfessionalMapper.java
│   └── ProfessionalValidator.java

├── appointment/                         # TIER 3 - EMPTY SHELLS
│   ├── AppointmentController.java
│   ├── AppointmentService.java
│   ├── AppointmentRepository.java
│   ├── Appointment.java
│   ├── AppointmentDto.java
│   ├── AppointmentFactory.java
│   ├── AppointmentMapper.java
│   └── AppointmentValidator.java

├── session/                             # TIER 3 - EMPTY SHELLS
│   ├── SessionController.java
│   ├── SessionService.java
│   ├── SessionRepository.java
│   ├── TherapySession.java
│   ├── SessionDto.java
│   ├── SessionFactory.java
│   ├── SessionMapper.java
│   └── SessionValidator.java

├── triage/                              # TIER 3 - EMPTY SHELLS
│   ├── TriageController.java
│   ├── TriageService.java
│   ├── TriageRepository.java
│   ├── Triage.java
│   ├── TriageDto.java
│   ├── TriageFactory.java
│   ├── TriageMapper.java
│   └── TriageValidator.java

├── anamnesis/                           # TIER 3 - EMPTY SHELLS
│   ├── AnamnesisController.java
│   ├── AnamnesisService.java
│   ├── AnamnesisRepository.java
│   ├── Anamnesis.java
│   ├── AnamnesisComplaint.java
│   ├── AnamnesisEmotion.java
│   ├── AnamnesisComplaints.java
│   ├── AnamnesisEmotions.java
│   ├── AnamnesisDto.java
│   ├── AnamnesisFactory.java
│   ├── AnamnesisMapper.java
│   └── AnamnesisValidator.java

├── report/                              # TIER 3 - EMPTY SHELLS
│   ├── ReportController.java
│   ├── ReportService.java
│   ├── ReportRepository.java
│   ├── Report.java
│   ├── ReportDto.java
│   ├── ReportFactory.java
│   ├── ReportMapper.java
│   └── ReportValidator.java

├── cryptography/                        # TIER 3 - EMPTY SHELLS
│   ├── CryptographyController.java
│   ├── CryptographyService.java
│   ├── KeyReferenceRepository.java
│   ├── PatientKeyReferenceRepository.java
│   ├── KeyReference.java
│   ├── PatientKeyReference.java
│   ├── CryptographyDto.java
│   ├── CryptographyFactory.java
│   ├── CryptographyMapper.java
│   └── CryptographyValidator.java
│   # Note: backend does NOT encrypt/decrypt. Only stores professional public
│   # keys/fingerprints (Key_Reference) and wrapped patient DEKs
│   # (Patient_Key_Reference). Key generation/wrapping happens on the frontend.

└── audit/                               # TIER 3 - EMPTY SHELLS
    ├── AuditController.java
    ├── AuditService.java
    ├── AuditLogRepository.java
    ├── AuditLog.java
    ├── AuditDto.java
    ├── AuditFactory.java
    ├── AuditMapper.java
    └── AuditValidator.java

domain/                                  # TIER 1 - FULLY FUNCTIONAL (shared lookup tables)
├── UserType.java / UserTypeRepository.java
├── ModalityType.java / ModalityTypeRepository.java
├── Specialty.java / SpecialtyRepository.java
├── ProfessionalLevel.java / ProfessionalLevelRepository.java
├── PatientStatus.java / PatientStatusRepository.java
├── SessionStatus.java / SessionStatusRepository.java
├── TargetAudience.java / TargetAudienceRepository.java
├── Gender.java / GenderRepository.java
├── EducationLevel.java / EducationLevelRepository.java
├── Ethnicity.java / EthnicityRepository.java
├── Tag.java / TagRepository.java
├── Room.java / RoomRepository.java
└── KeyStatus.java / KeyStatusRepository.java

src/main/resources/
├── application.yml
├── application-dev.yml
├── application-prod.yml
└── db/migration/
    ├── V1__init_schema.sql              # generated from schema.dbml — TIER 1
    ├── V2__insert_domain_data.sql       # follow-up once values finalized
    └── V3__insert_rbac_data.sql         # follow-up once values finalized

src/test/java/com/unisantos/clinica_api/
└── patient/
    └── PatientServiceTest.java          # the ONLY real test class (TIER 2 template)

# No test packages/stubs for Tier 3 features. Each team writes its own tests
# when it implements its feature, using PatientServiceTest as the template.
```

**No per-feature `XxxConfig.java` files anywhere** — skip entirely unless a feature is later found to genuinely need feature-specific beans.

---

## Tier 1 Components — Detail

1. **Build tooling** — Spotless (Google Java Format) + Checkstyle (with Tier 3 suppression filter) + Gradle, as above.
2. **`common/`** — `GlobalExceptionHandler`, `ApiResponse`, `PageResponse`, `ErrorResponse`, the three exceptions, `JwtUtil`, `PasswordUtil`, `EntityFactory` interface, `SecurityConstants`, `AppConstants`.
3. **`security/`** — JWT filter/provider, `CustomUserDetailsService`, `PermissionStrategy` interface, and **all three concrete strategies fully implemented from `permissions-matrix.md`** (not guessed — this is shared logic every feature depends on), `PermissionService`, `PermissionContext`.
4. **`config/`** — `SecurityConfig`, `CorsConfig` (real origins/methods/headers from env vars, documented in `.env.example`, no hardcoded production origin), `SwaggerConfig` (SpringDoc at `/swagger-ui.html`), `FlywayConfig`, `JpaConfig`.
5. **`auth/`** — real login/register/refresh/logout/forgot-password/reset-password. Every other feature's manual QA depends on this working.
6. **CORS end-to-end** — since frontend and backend share this repo, the real `client/src/core/api/client.ts` (see below) is built now, not deferred.
7. **`domain/`** — all lookup entities/repositories, read directly from `schema.dbml`.
8. **`V1__init_schema.sql`** — generated directly from `schema.dbml`. `V2`/`V3` seed data are follow-ups once those values are finalized.

---

## Tier 2 — Patient Reference Implementation (Fully Functional)

### PatientFactory (excerpt)
```java
/**
 * Factory for creating Patient entities.
 *
 * Zero-Trust: does NOT encrypt/decrypt. The DTO already carries encrypted
 * fields as VARBINARY/byte[]. The factory only checks presence — it never
 * inspects, compares, or de-duplicates CPF or any other encrypted value.
 */
@Component
public class PatientFactory implements EntityFactory<Patient> {

    private final PatientRepository patientRepository;
    private final PatientStatusRepository patientStatusRepository;

    public PatientFactory(PatientRepository patientRepository,
                           PatientStatusRepository patientStatusRepository) {
        this.patientRepository = patientRepository;
        this.patientStatusRepository = patientStatusRepository;
    }

    @Override
    public Patient create(PatientDto dto) {
        if (dto.getFirstName() == null || dto.getFirstName().length == 0) {
            throw new ValidationException("firstName is required");
        }
        if (dto.getLastName() == null || dto.getLastName().length == 0) {
            throw new ValidationException("lastName is required");
        }
        if (dto.getCpf() == null || dto.getCpf().length == 0) {
            throw new ValidationException("cpf is required");
        }

        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setCpf(dto.getCpf());
        patient.setBirthDate(dto.getBirthDate());
        patient.setProfessionalId(dto.getProfessionalId());
        patient.setStatus(patientStatusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("PatientStatus ACTIVE not found")));
        patient.setRegisteredAt(LocalDateTime.now());
        return patient;
    }

    public Patient update(Long id, PatientDto dto) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getCpf() != null) existing.setCpf(dto.getCpf());
        if (dto.getBirthDate() != null) existing.setBirthDate(dto.getBirthDate());
        return existing;
    }
}
```

### PatientService (complete — includes the two previously-missing methods)
```java
@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientFactory patientFactory;
    private final PatientMapper patientMapper;
    private final PermissionService permissionService;
    private final AuditService auditService;
    private final PatientStatusRepository patientStatusRepository;

    public PatientService(PatientRepository patientRepository, PatientFactory patientFactory,
                           PatientMapper patientMapper, PermissionService permissionService,
                           AuditService auditService, PatientStatusRepository patientStatusRepository) {
        this.patientRepository = patientRepository;
        this.patientFactory = patientFactory;
        this.patientMapper = patientMapper;
        this.permissionService = permissionService;
        this.auditService = auditService;
        this.patientStatusRepository = patientStatusRepository;
    }

    public PatientDto registerPatient(PatientDto dto, Long currentUserId) {
        if (!permissionService.canCreate("patient", currentUserId)) {
            throw new PermissionDeniedException("User does not have permission to create patients");
        }
        Patient patient = patientFactory.create(dto);
        patient.setRegisteredByUser(currentUserId);
        Patient saved = patientRepository.save(patient);
        auditService.log("PATIENT_CREATED", saved.getId(), currentUserId);
        return patientMapper.toDto(saved);
    }

    public PatientDto updatePatient(Long patientId, PatientDto dto, Long currentUserId) {
        if (!permissionService.canUpdate("patient", currentUserId, patientId)) {
            throw new PermissionDeniedException("User does not have permission to update this patient");
        }
        Patient updated = patientFactory.update(patientId, dto);
        Patient saved = patientRepository.save(updated);
        auditService.log("PATIENT_UPDATED", patientId, currentUserId);
        return patientMapper.toDto(saved);
    }

    /**
     * Updates only the patient's status (ACTIVE, DEACTIVATED, DISCHARGED).
     * Does NOT touch any other field — implemented separately from
     * updatePatient() so it never overwrites encrypted fields with nulls.
     */
    public PatientDto updateStatus(Long patientId, String statusName, Long currentUserId) {
        if (!permissionService.canUpdate("patient", currentUserId, patientId)) {
            throw new PermissionDeniedException("User does not have permission to update this patient's status");
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        PatientStatus status = patientStatusRepository.findByName(statusName)
                .orElseThrow(() -> new ValidationException("Invalid status: " + statusName));
        patient.setStatus(status);
        Patient saved = patientRepository.save(patient);
        auditService.log("PATIENT_STATUS_UPDATED", patientId, currentUserId);
        return patientMapper.toDto(saved);
    }

    /**
     * Assigns or changes the responsible psychologist. Coordinator-only —
     * enforced via permissionService, not just the controller layer.
     * Does NOT touch any other field. Re-enveloping the patient's encrypted
     * DEK for the new psychologist's public key is a frontend responsibility
     * triggered after this call succeeds (see "Excluded" section).
     */
    public PatientDto assignPsychologist(Long patientId, Long professionalId, Long currentUserId) {
        if (!permissionService.isCoordinator(currentUserId)) {
            throw new PermissionDeniedException("Only coordinators can assign a psychologist");
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        patient.setProfessionalId(professionalId);
        Patient saved = patientRepository.save(patient);
        auditService.log("PATIENT_PSYCHOLOGIST_ASSIGNED", patientId, currentUserId);
        return patientMapper.toDto(saved);
    }

    public PatientDto getPatientById(Long patientId, Long currentUserId) {
        if (!permissionService.canRead("patient", currentUserId, patientId)) {
            throw new PermissionDeniedException("User does not have permission to view this patient");
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        return patientMapper.toDto(patient);
    }

    public Page<PatientDto> getAllPatients(Long currentUserId, Pageable pageable) {
        Page<Patient> patients = permissionService.isPsychologist(currentUserId)
                ? patientRepository.findByProfessionalId(currentUserId, pageable)
                : patientRepository.findAll(pageable);
        return patients.map(patientMapper::toDto);
    }
}
```

### PatientController (complete — the two previously-broken endpoints now call the real methods above)
```java
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Patient management API")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Register a new patient")
    @PostMapping
    public ResponseEntity<ApiResponse<PatientDto>> registerPatient(
            @Valid @RequestBody PatientDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = extractUserId(userDetails);
        PatientDto result = patientService.registerPatient(dto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDto>> getPatient(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        PatientDto result = patientService.getPatientById(id, extractUserId(userDetails));
        return ResponseEntity.ok(new ApiResponse<>(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PatientDto>>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @AuthenticationPrincipal UserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(sortBy));
        Page<PatientDto> result = patientService.getAllPatients(extractUserId(userDetails), pageable);
        return ResponseEntity.ok(new ApiResponse<>(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDto>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        PatientDto result = patientService.updatePatient(id, dto, extractUserId(userDetails));
        return ResponseEntity.ok(new ApiResponse<>(result));
    }

    /** PATCH /api/v1/patients/{id}/status — body: {"status": "DEACTIVATED"} */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PatientDto>> updatePatientStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate,
            @AuthenticationPrincipal UserDetails userDetails) {
        String status = statusUpdate.get("status");
        if (status == null || status.isBlank()) {
            throw new ValidationException("status is required");
        }
        PatientDto result = patientService.updateStatus(id, status, extractUserId(userDetails));
        return ResponseEntity.ok(new ApiResponse<>(result));
    }

    /** PATCH /api/v1/patients/{id}/psychologist — body: {"professionalId": 7} — COORDINATOR ONLY */
    @PatchMapping("/{id}/psychologist")
    public ResponseEntity<ApiResponse<PatientDto>> assignPsychologist(
            @PathVariable Long id,
            @RequestBody Map<String, Long> assignment,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long professionalId = assignment.get("professionalId");
        if (professionalId == null) {
            throw new ValidationException("professionalId is required");
        }
        PatientDto result = patientService.assignPsychologist(id, professionalId, extractUserId(userDetails));
        return ResponseEntity.ok(new ApiResponse<>(result));
    }

    private Long extractUserId(UserDetails userDetails) {
        // TODO: replace with real extraction once CustomUserDetailsService's
        // principal type is finalized (e.g. a custom UserPrincipal with a getId()).
        return Long.parseLong(userDetails.getUsername());
    }
}
```

### PatientServiceTest (template for other features)
```java
/**
 * Reference test class — the template other developers copy when they
 * implement tests for their own Tier 3 feature.
 *
 * Test scenarios based on business rules:
 * - Secretary can register patients (permission check)
 * - Coordinator can register patients (permission check)
 * - Psychologist cannot register patients (permission denied)
 * - CPF bytes are stored and returned as-is — never validated, compared,
 *   or checked for uniqueness server-side (opaque encrypted VARBINARY)
 * - updateStatus() only changes status, leaves other fields untouched
 * - assignPsychologist() is rejected for non-coordinator roles
 *
 * Reference: Permissions Matrix section 2.3
 * Reference: Database schema - Patient table
 */
class PatientServiceTest {
    // TODO: test secretary registering a patient (permission granted)
    // TODO: test coordinator registering a patient (permission granted)
    // TODO: test psychologist attempting to register a patient (permission denied)
    // TODO: test CPF bytes round-trip untouched (no backend validation/comparison)
    // TODO: test updateStatus() changes only status field
    // TODO: test assignPsychologist() succeeds for coordinator, denied for others
}
```

---

## Tier 3 — Empty Shells (all other features)

For `professional`, `appointment`, `session`, `triage`, `anamnesis`, `report`, `cryptography`, `audit`, `user`, generate **only**:

- Entity class(es) with JPA annotations matching `schema.dbml` — fields and relationships, no business methods.
- Repository interface extending `JpaRepository<Entity, Long>` — empty.
- DTO — fields only, `@NotNull`/`@NotBlank` only where the schema marks a column non-nullable.
- `Mapper`, `Factory`, `Validator`, `Service`, `Controller` — class definition only: correct name, package, `@Component`/`@Service`/`@RestController` annotation, constructor with injected dependencies. **No method bodies, no TODOs, no CS50-style comments, no Javadoc essays.**

One line per class is enough:
```java
// Skeleton only — see patient/PatientService.java for the reference implementation and pattern to follow.
@Service
public class ProfessionalService {
    private final ProfessionalRepository professionalRepository;
    private final PermissionService permissionService;

    public ProfessionalService(ProfessionalRepository professionalRepository, PermissionService permissionService) {
        this.professionalRepository = professionalRepository;
        this.permissionService = permissionService;
    }
}
```

**Do not generate**: per-feature `XxxConfig.java` files, or any test files for these features.

---

## CORS Implementation (Tier 1 — Fully Functional, Both Sides)

### Backend — CorsConfig.java
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins:http://localhost:3000}")
    private String[] allowedOrigins;

    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
    private String[] allowedMethods;

    @Value("${app.cors.allowed-headers:Authorization,Content-Type}")
    private String[] allowedHeaders;

    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${app.cors.max-age:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }
}
```
The `localhost:3000` default is dev-only — the deployed profile must override `app.cors.allowed-origins` explicitly; fail startup rather than silently falling back to it in `prod`.

### Frontend — client/src/core/api/client.ts
```typescript
/**
 * Zero-Trust Token Flow:
 * - Access token: in-memory only (never localStorage/sessionStorage)
 * - Refresh token: httpOnly cookie, set by backend, sent automatically by
 *   the browser via withCredentials — never touched by JS
 */
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

let accessToken: string | null = null;

export function setAccessToken(token: string): void {
  accessToken = token;
}
export function getAccessToken(): string | null {
  return accessToken;
}
export function clearAccessToken(): void {
  accessToken = null;
}

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: { 'Content-Type': 'application/json' },
});

apiClient.interceptors.request.use(
  (config) => {
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const response = await axios.post(
          `${API_BASE_URL}/api/v1/auth/refresh`,
          {},
          { withCredentials: true }
        );
        setAccessToken(response.data.accessToken);
        originalRequest.headers.Authorization = `Bearer ${response.data.accessToken}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        clearAccessToken();
        window.location.href = '/login';
      }
    }

    if (error.response?.status === 403) {
      console.error('Permission denied:', error.response.data);
    }

    return Promise.reject(error);
  }
);

export default apiClient;
```

---

## Permission Flow

1. Controller receives request with JWT in `Authorization` header.
2. `JwtAuthenticationFilter` validates the token, sets `SecurityContext`.
3. Controller extracts current user ID from the authentication principal.
4. Controller calls the service method with that user ID.
5. Service calls `PermissionService`, which selects the right `PermissionStrategy` by role.
6. Strategy applies role rules; on denial, throws `PermissionDeniedException`.
7. `GlobalExceptionHandler` turns that into a `403` response.

### Row-Level Security (OWN scope)
- Psychologist: `resource.professionalId == currentUserId`.
- Coordinator/Secretary: scope ALL, no ownership check.

---

## Implementation Phases (single, authoritative list)

**Phase 1 — Build tooling**
Spotless, Checkstyle (with Tier 3 suppression filter), Gradle plugins.

**Phase 2 — Tier 1 shared contracts**
`common/`, `security/` (including the three real permission strategies), `config/`, `auth/`, `domain/` entities from `schema.dbml`, `V1__init_schema.sql`, CORS end-to-end (backend + frontend `client.ts`).

**Phase 3 — Tier 2 reference implementation**
Full `patient/` feature (entities, repository, DTO, factory, mapper, validator, service — including `updateStatus` and `assignPsychologist` — controller, Swagger annotations) plus `PatientServiceTest`.

**Phase 4 — Tier 3 empty shells**
For each remaining feature: entity classes (from `schema.dbml`), repository interfaces, DTOs, and empty `Mapper`/`Factory`/`Validator`/`Service`/`Controller` shells. No config classes, no test stubs.

**Phase 5 — Verification**
Confirm the "Definition of Done" checklist below.

---

## SOLID Principles Application

- **SRP**: services = business logic, controllers = HTTP concerns, repositories = data access, factories = object creation, strategies = permission logic.
- **OCP**: new permission strategies/factories/validators added without modifying existing code.
- **LSP**: `PermissionStrategy` and `EntityFactory` implementations are fully interchangeable.
- **ISP**: `PermissionStrategy` methods are focused (`canCreate`, `canRead`, `canUpdate`, `canDelete`); no fat interfaces.
- **DIP**: services depend on repository interfaces; controllers depend on service classes; `PermissionService` depends on the `PermissionStrategy` interface.

---

## Definition of Done

- Project builds and passes Checkstyle/Spotless.
- `auth` login/refresh/logout works end-to-end against a dev database migrated from `V1__init_schema.sql`.
- Frontend calls the backend across origins using the real CORS + in-memory-token/httpOnly-cookie flow (no `localStorage` token anywhere).
- `patient` feature is fully operable via Swagger UI, including status update and psychologist assignment, respecting the three real permission strategies.
- All other features compile with empty, correctly-shaped, correctly-injected classes — nothing more.

## Excluded From This Pass

- Concrete cryptography design (key-wrapping format, revocation flow, re-enveloping trigger/consistency after `assignPsychologist`) — Tier 3 table/entity shells only; the actual design is a separate task.
- Async/decoupled audit logging — currently synchronous in the service layer; the sync-vs-event-driven question stays open.
- Rate limiting on `/auth/login` and `/auth/forgot-password` — flag as a follow-up ticket.
- RBAC/domain seed data values beyond what's already fixed in the permissions matrix.
