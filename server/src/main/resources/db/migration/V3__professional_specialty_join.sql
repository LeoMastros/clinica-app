-- ==========================================================
-- V3 — Professional ↔ Specialty becomes many-to-many.
-- A psychologist can have multiple specialties; replaces the
-- single Professional.id_specialty FK with a join table,
-- following the Professional_Target_Audience pattern.
-- ==========================================================

ALTER TABLE Professional DROP FOREIGN KEY fk_professional_specialty;
ALTER TABLE Professional DROP COLUMN id_specialty;

CREATE TABLE Professional_Specialty (
  id_professional_specialty INT AUTO_INCREMENT PRIMARY KEY,
  id_professional INT NOT NULL,
  id_specialty INT NOT NULL,
  CONSTRAINT fk_ps_professional FOREIGN KEY (id_professional)
    REFERENCES Professional (id_professional),
  CONSTRAINT fk_ps_specialty FOREIGN KEY (id_specialty)
    REFERENCES Specialty (id_specialty),
  CONSTRAINT uq_professional_specialty UNIQUE (id_professional, id_specialty)
);
