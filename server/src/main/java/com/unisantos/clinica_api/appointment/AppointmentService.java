package com.unisantos.clinica_api.appointment;

import com.unisantos.clinica_api.security.permissions.PermissionService;
import org.springframework.stereotype.Service;

// Skeleton only — see patient/PatientService.java for the reference implementation and pattern to
// follow.
@Service
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final PermissionService permissionService;

  public AppointmentService(
      AppointmentRepository appointmentRepository, PermissionService permissionService) {
    this.appointmentRepository = appointmentRepository;
    this.permissionService = permissionService;
  }
}
