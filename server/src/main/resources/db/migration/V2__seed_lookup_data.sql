-- ==========================================================
-- PsiUnisantos — Lookup seed data (V2)
-- Values taken from the enumerated notes in schema.dbml.
-- Gender/Education_Level/Ethnicity/Tag/Room are intentionally
-- left empty: schema.dbml gives no values for them.
-- ==========================================================

INSERT INTO User_Type (type_name) VALUES
  ('COORDINATOR'),
  ('SECRETARY'),
  ('PROFESSIONAL');

INSERT INTO Modality_Type (modality_name) VALUES
  ('IN_PERSON'),
  ('ONLINE');

INSERT INTO Patient_Status (status_name) VALUES
  ('ACTIVE'),
  ('DEACTIVATED'),
  ('DISCHARGED');

INSERT INTO Session_Status (status_name) VALUES
  ('SCHEDULED'),
  ('COMPLETED'),
  ('NO_SHOW'),
  ('CANCELLED');

INSERT INTO Key_Status (status_name) VALUES
  ('ACTIVE'),
  ('REVOKED');

-- Example values listed in schema.dbml notes.
INSERT INTO Specialty (specialty_name) VALUES
  ('CBT'),
  ('Psychoanalysis');

INSERT INTO Professional_Level (level_name) VALUES
  ('Intern Student'),
  ('Recently Graduated');

INSERT INTO Target_Audience (audience_name) VALUES
  ('Children'),
  ('Adolescents');
