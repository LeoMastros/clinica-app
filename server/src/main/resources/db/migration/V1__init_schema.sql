-- ==========================================================
-- PsiUnisantos — Initial Schema (V1)
-- MySQL 8 target. Generated from schema.dbml.
-- Personal/clinical fields are VARBINARY/BLOB ciphertext.
-- ==========================================================

-- ----------------------------------------------------------
-- Domain tables (types and classifications)
-- ----------------------------------------------------------

CREATE TABLE User_Type (
  id_user_type INT AUTO_INCREMENT PRIMARY KEY,
  type_name VARCHAR(30) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Modality_Type (
  id_modality_type INT AUTO_INCREMENT PRIMARY KEY,
  modality_name VARCHAR(20) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Specialty (
  id_specialty INT AUTO_INCREMENT PRIMARY KEY,
  specialty_name VARCHAR(80) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Professional_Level (
  id_level INT AUTO_INCREMENT PRIMARY KEY,
  level_name VARCHAR(60) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Patient_Status (
  id_patient_status INT AUTO_INCREMENT PRIMARY KEY,
  status_name VARCHAR(30) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Session_Status (
  id_session_status INT AUTO_INCREMENT PRIMARY KEY,
  status_name VARCHAR(30) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Target_Audience (
  id_audience INT AUTO_INCREMENT PRIMARY KEY,
  audience_name VARCHAR(60) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Gender (
  id_gender INT AUTO_INCREMENT PRIMARY KEY,
  gender_name VARCHAR(30) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Education_Level (
  id_education_level INT AUTO_INCREMENT PRIMARY KEY,
  education_level_name VARCHAR(60) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Ethnicity (
  id_ethnicity INT AUTO_INCREMENT PRIMARY KEY,
  ethnicity_name VARCHAR(60) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Tag (
  id_tag INT AUTO_INCREMENT PRIMARY KEY,
  tag_name VARCHAR(40) NOT NULL UNIQUE,
  hex_color VARCHAR(7) NULL
) ENGINE = InnoDB;

CREATE TABLE Room (
  id_room INT AUTO_INCREMENT PRIMARY KEY,
  room_name VARCHAR(60) NOT NULL UNIQUE,
  is_active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE = InnoDB;

CREATE TABLE Key_Status (
  id_key_status INT AUTO_INCREMENT PRIMARY KEY,
  status_name VARCHAR(20) NOT NULL UNIQUE
) ENGINE = InnoDB;

-- ----------------------------------------------------------
-- Identity and authentication
-- ----------------------------------------------------------

CREATE TABLE User (
  id_user INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARBINARY(255) NULL,
  last_name VARBINARY(255) NULL,
  login_email VARCHAR(320) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  id_user_type INT NOT NULL,
  registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT fk_user_user_type FOREIGN KEY (id_user_type)
    REFERENCES User_Type (id_user_type)
) ENGINE = InnoDB;

CREATE TABLE Refresh_Token (
  id_refresh_token BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_user INT NOT NULL,
  token_hash BINARY(32) NOT NULL UNIQUE,
  family_id BIGINT NOT NULL,
  replaced_by_token BIGINT NULL,
  issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL,
  used_at TIMESTAMP NULL,
  revoked_at TIMESTAMP NULL,
  ip_address VARBINARY(45) NULL,
  user_agent VARBINARY(255) NULL,
  CONSTRAINT fk_refresh_token_user FOREIGN KEY (id_user)
    REFERENCES User (id_user),
  CONSTRAINT fk_refresh_token_replaced_by FOREIGN KEY (replaced_by_token)
    REFERENCES Refresh_Token (id_refresh_token)
) ENGINE = InnoDB;

CREATE TABLE Password_Reset_Token (
  id_password_reset BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_user INT NOT NULL,
  token_hash BINARY(32) NOT NULL UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL,
  used_at TIMESTAMP NULL,
  CONSTRAINT fk_password_reset_user FOREIGN KEY (id_user)
    REFERENCES User (id_user)
) ENGINE = InnoDB;

-- ----------------------------------------------------------
-- Professional
-- ----------------------------------------------------------

CREATE TABLE Professional (
  id_professional INT AUTO_INCREMENT PRIMARY KEY,
  id_user INT NOT NULL UNIQUE,
  id_professional_level INT NOT NULL,
  id_specialty INT NOT NULL,
  cpf VARBINARY(512) NULL,
  phone VARBINARY(512) NULL,
  birth_date VARBINARY(64) NULL,
  crp_registration VARBINARY(512) NULL,
  identification_color VARCHAR(7) NULL,
  CONSTRAINT fk_professional_user FOREIGN KEY (id_user)
    REFERENCES User (id_user),
  CONSTRAINT fk_professional_level FOREIGN KEY (id_professional_level)
    REFERENCES Professional_Level (id_level),
  CONSTRAINT fk_professional_specialty FOREIGN KEY (id_specialty)
    REFERENCES Specialty (id_specialty)
) ENGINE = InnoDB;

CREATE TABLE Professional_Target_Audience (
  id_professional_audience INT AUTO_INCREMENT PRIMARY KEY,
  id_professional INT NOT NULL,
  id_audience INT NOT NULL,
  CONSTRAINT fk_pta_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional),
  CONSTRAINT fk_pta_audience FOREIGN KEY (id_audience)
    REFERENCES Target_Audience (id_audience),
  CONSTRAINT uq_professional_audience UNIQUE (id_professional, id_audience)
) ENGINE = InnoDB;

CREATE TABLE Professional_Availability (
  id_availability INT AUTO_INCREMENT PRIMARY KEY,
  id_professional INT NOT NULL,
  day_of_week TINYINT NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  CONSTRAINT fk_availability_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional),
  CONSTRAINT chk_availability_time CHECK (end_time > start_time)
) ENGINE = InnoDB;

-- ----------------------------------------------------------
-- Patient
-- ----------------------------------------------------------

CREATE TABLE Patient (
  id_patient INT AUTO_INCREMENT PRIMARY KEY,
  id_professional INT NOT NULL,
  id_patient_status INT NOT NULL,
  id_gender INT NULL,
  id_education_level INT NULL,
  id_ethnicity INT NULL,
  first_name VARBINARY(255) NOT NULL,
  last_name VARBINARY(255) NOT NULL,
  social_name VARBINARY(255) NULL,
  contact_email VARBINARY(255) NULL,
  mobile_phone VARBINARY(255) NULL,
  landline_phone VARBINARY(255) NULL,
  cpf VARBINARY(512) NULL,
  rg VARBINARY(512) NULL,
  birth_date VARBINARY(64) NOT NULL,
  place_of_birth VARBINARY(120) NULL,
  occupation VARBINARY(120) NULL,
  how_they_found_us VARBINARY(120) NULL,
  referred_by VARBINARY(120) NULL,
  identification_color VARCHAR(7) NULL,
  notes BLOB NULL,
  registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  registered_by_user INT NOT NULL,
  CONSTRAINT fk_patient_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional),
  CONSTRAINT fk_patient_status FOREIGN KEY (id_patient_status)
    REFERENCES Patient_Status (id_patient_status),
  CONSTRAINT fk_patient_gender FOREIGN KEY (id_gender)
    REFERENCES Gender (id_gender),
  CONSTRAINT fk_patient_education FOREIGN KEY (id_education_level)
    REFERENCES Education_Level (id_education_level),
  CONSTRAINT fk_patient_ethnicity FOREIGN KEY (id_ethnicity)
    REFERENCES Ethnicity (id_ethnicity),
  CONSTRAINT fk_patient_registered_by FOREIGN KEY (registered_by_user)
    REFERENCES User (id_user)
) ENGINE = InnoDB;

CREATE TABLE Patient_Address (
  id_address INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL UNIQUE,
  country VARBINARY(60) NULL,
  zip_code VARBINARY(12) NULL,
  state VARBINARY(60) NULL,
  city VARBINARY(80) NULL,
  neighborhood VARBINARY(80) NULL,
  street VARBINARY(150) NULL,
  number VARBINARY(20) NULL,
  complement VARBINARY(80) NULL,
  CONSTRAINT fk_patient_address_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient)
) ENGINE = InnoDB;

CREATE TABLE Patient_Guardian (
  id_guardian INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL UNIQUE,
  guardian_name VARBINARY(255) NOT NULL,
  guardian_email VARBINARY(255) NULL,
  guardian_mobile_phone VARBINARY(255) NULL,
  guardian_cpf VARBINARY(512) NULL,
  guardian_rg VARBINARY(512) NULL,
  guardian_birth_date VARBINARY(64) NULL,
  allow_document_billing BOOLEAN NOT NULL DEFAULT FALSE,
  allow_session_reminders BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_patient_guardian_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient)
) ENGINE = InnoDB;

CREATE TABLE Emergency_Contact (
  id_emergency_contact INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL,
  name VARBINARY(255) NOT NULL,
  relationship VARBINARY(60) NULL,
  phone VARBINARY(255) NOT NULL,
  is_primary BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_emergency_contact_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient)
) ENGINE = InnoDB;

CREATE TABLE Patient_Tag (
  id_patient_tag INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL,
  id_tag INT NOT NULL,
  CONSTRAINT fk_patient_tag_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient),
  CONSTRAINT fk_patient_tag_tag FOREIGN KEY (id_tag)
    REFERENCES Tag (id_tag),
  CONSTRAINT uq_patient_tag UNIQUE (id_patient, id_tag)
) ENGINE = InnoDB;

-- ----------------------------------------------------------
-- Scheduling and care
-- ----------------------------------------------------------

CREATE TABLE Therapy_Session (
  id_session INT AUTO_INCREMENT PRIMARY KEY,
  id_professional INT NOT NULL,
  id_patient INT NOT NULL,
  id_modality_type INT NOT NULL,
  id_room INT NULL,
  id_session_status INT NOT NULL,
  start_datetime DATETIME NOT NULL,
  end_datetime DATETIME NOT NULL,
  is_recurring BOOLEAN NOT NULL DEFAULT FALSE,
  virtual_room_token VARBINARY(64) NULL,
  virtual_room_expires DATETIME NULL,
  scheduled_by_user INT NOT NULL,
  no_show_processed_at TIMESTAMP NULL,
  CONSTRAINT fk_session_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional),
  CONSTRAINT fk_session_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient),
  CONSTRAINT fk_session_modality FOREIGN KEY (id_modality_type)
    REFERENCES Modality_Type (id_modality_type),
  CONSTRAINT fk_session_room FOREIGN KEY (id_room)
    REFERENCES Room (id_room),
  CONSTRAINT fk_session_status FOREIGN KEY (id_session_status)
    REFERENCES Session_Status (id_session_status),
  CONSTRAINT fk_session_scheduled_by FOREIGN KEY (scheduled_by_user)
    REFERENCES User (id_user),
  CONSTRAINT chk_session_datetime CHECK (end_datetime > start_datetime)
) ENGINE = InnoDB;

CREATE TABLE Triage (
  id_triage INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL UNIQUE,
  id_professional INT NOT NULL,
  triage_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  initial_complaint BLOB NOT NULL,
  referral_recommendation BLOB NULL,
  notes BLOB NULL,
  CONSTRAINT fk_triage_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient),
  CONSTRAINT fk_triage_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional)
) ENGINE = InnoDB;

CREATE TABLE Anamnesis_Complaints (
  id_complaint INT AUTO_INCREMENT PRIMARY KEY,
  complaint_name VARCHAR(120) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Anamnesis_Emotions (
  id_emotion INT AUTO_INCREMENT PRIMARY KEY,
  emotion_name VARCHAR(80) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Anamnesis (
  id_anamnesis INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL UNIQUE,
  id_professional INT NOT NULL,
  opening_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  family_history BLOB NULL,
  diagnostic_hypothesis BLOB NULL,
  general_notes BLOB NULL,
  treatment_plan BLOB NULL,
  referrals BLOB NULL,
  CONSTRAINT fk_anamnesis_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient),
  CONSTRAINT fk_anamnesis_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional)
) ENGINE = InnoDB;

CREATE TABLE Anamnesis_Complaint (
  id_anamnesis_complaint INT AUTO_INCREMENT PRIMARY KEY,
  id_anamnesis INT NOT NULL,
  id_complaint INT NOT NULL,
  CONSTRAINT fk_ac_anamnesis FOREIGN KEY (id_anamnesis)
    REFERENCES Anamnesis (id_anamnesis),
  CONSTRAINT fk_ac_complaint FOREIGN KEY (id_complaint)
    REFERENCES Anamnesis_Complaints (id_complaint),
  CONSTRAINT uq_anamnesis_complaint UNIQUE (id_anamnesis, id_complaint)
) ENGINE = InnoDB;

CREATE TABLE Anamnesis_Emotion (
  id_anamnesis_emotion INT AUTO_INCREMENT PRIMARY KEY,
  id_anamnesis INT NOT NULL,
  id_emotion INT NOT NULL,
  CONSTRAINT fk_ae_anamnesis FOREIGN KEY (id_anamnesis)
    REFERENCES Anamnesis (id_anamnesis),
  CONSTRAINT fk_ae_emotion FOREIGN KEY (id_emotion)
    REFERENCES Anamnesis_Emotions (id_emotion),
  CONSTRAINT uq_anamnesis_emotion UNIQUE (id_anamnesis, id_emotion)
) ENGINE = InnoDB;

CREATE TABLE Report (
  id_report INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL,
  id_professional INT NOT NULL,
  report_type VARCHAR(40) NOT NULL,
  issue_date DATE NOT NULL,
  content BLOB NOT NULL,
  file_path VARCHAR(255) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_report_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient),
  CONSTRAINT fk_report_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional)
) ENGINE = InnoDB;

CREATE TABLE Medical_Record_Transfer (
  id_transfer INT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL,
  id_professional_from INT NOT NULL,
  id_professional_to INT NOT NULL,
  transferred_by_user INT NOT NULL,
  transfer_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  reason VARBINARY(255) NULL,
  CONSTRAINT fk_mrt_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient),
  CONSTRAINT fk_mrt_prof_from FOREIGN KEY (id_professional_from)
    REFERENCES Professional (id_professional),
  CONSTRAINT fk_mrt_prof_to FOREIGN KEY (id_professional_to)
    REFERENCES Professional (id_professional),
  CONSTRAINT fk_mrt_transferred_by FOREIGN KEY (transferred_by_user)
    REFERENCES User (id_user)
) ENGINE = InnoDB;

-- ----------------------------------------------------------
-- Cryptography / chain of trust
-- ----------------------------------------------------------

CREATE TABLE Key_Reference (
  id_key_reference INT AUTO_INCREMENT PRIMARY KEY,
  id_professional INT NOT NULL,
  public_key VARBINARY(4096) NOT NULL,
  key_fingerprint VARBINARY(32) NOT NULL,
  trust_certificate VARBINARY(4096) NOT NULL,
  id_key_status INT NOT NULL,
  generated_by_user INT NOT NULL,
  generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  revoked_at TIMESTAMP NULL,
  CONSTRAINT fk_key_ref_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional),
  CONSTRAINT fk_key_ref_status FOREIGN KEY (id_key_status)
    REFERENCES Key_Status (id_key_status),
  CONSTRAINT fk_key_ref_generated_by FOREIGN KEY (generated_by_user)
    REFERENCES User (id_user)
) ENGINE = InnoDB;

CREATE TABLE Patient_Key_Reference (
  id_reference BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_patient INT NOT NULL,
  id_key_reference INT NOT NULL,
  wrapped_dek BLOB NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  revoked_at TIMESTAMP NULL,
  CONSTRAINT fk_pkr_patient FOREIGN KEY (id_patient)
    REFERENCES Patient (id_patient),
  CONSTRAINT fk_pkr_key_ref FOREIGN KEY (id_key_reference)
    REFERENCES Key_Reference (id_key_reference),
  CONSTRAINT uq_patient_key_ref UNIQUE (id_patient, id_key_reference)
) ENGINE = InnoDB;

-- ----------------------------------------------------------
-- RBAC / authorization
-- ----------------------------------------------------------

CREATE TABLE Module (
  id_module INT AUTO_INCREMENT PRIMARY KEY,
  module_key VARCHAR(40) NOT NULL UNIQUE,
  module_name VARCHAR(80) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE Resource (
  id_resource INT AUTO_INCREMENT PRIMARY KEY,
  id_module INT NOT NULL,
  resource_key VARCHAR(80) NOT NULL UNIQUE,
  resource_name VARCHAR(150) NOT NULL,
  CONSTRAINT fk_resource_module FOREIGN KEY (id_module)
    REFERENCES Module (id_module)
) ENGINE = InnoDB;

CREATE TABLE Permission (
  id_permission INT AUTO_INCREMENT PRIMARY KEY,
  id_user_type INT NOT NULL,
  id_resource INT NOT NULL,
  can_create BOOLEAN NOT NULL DEFAULT FALSE,
  can_read BOOLEAN NOT NULL DEFAULT FALSE,
  can_update BOOLEAN NOT NULL DEFAULT FALSE,
  can_delete BOOLEAN NOT NULL DEFAULT FALSE,
  scope ENUM('ALL', 'OWN') NOT NULL DEFAULT 'ALL',
  CONSTRAINT fk_permission_user_type FOREIGN KEY (id_user_type)
    REFERENCES User_Type (id_user_type),
  CONSTRAINT fk_permission_resource FOREIGN KEY (id_resource)
    REFERENCES Resource (id_resource),
  CONSTRAINT uq_user_type_resource UNIQUE (id_user_type, id_resource)
) ENGINE = InnoDB;

-- ----------------------------------------------------------
-- Auditing
-- ----------------------------------------------------------

CREATE TABLE Audit_Log (
  id_audit BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_user INT NOT NULL,
  action VARCHAR(100) NOT NULL,
  entity_name VARCHAR(100) NOT NULL,
  entity_id BIGINT NULL,
  occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ip_address VARBINARY(45) NULL,
  user_agent VARBINARY(255) NULL,
  details BLOB NULL,
  CONSTRAINT fk_audit_user FOREIGN KEY (id_user)
    REFERENCES User (id_user)
) ENGINE = InnoDB;
