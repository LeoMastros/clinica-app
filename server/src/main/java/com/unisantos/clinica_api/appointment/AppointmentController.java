package com.unisantos.clinica_api.appointment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Skeleton only — see patient/PatientService.java for the reference implementation and pattern to
// follow.
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

  private final AppointmentService appointmentService;

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }
}
