package com.unisantos.clinica_api.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// No Appointment table exists in schema.dbml — scheduling is modeled by
// Therapy_Session. Kept as a plain class shell until the schema defines it.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

  private Long id;
}
