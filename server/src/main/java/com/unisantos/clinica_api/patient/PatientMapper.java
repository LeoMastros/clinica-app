package com.unisantos.clinica_api.patient;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

  public PatientDto toDto(Patient patient) {
    if (patient == null) {
      return null;
    }

    return PatientDto.builder()
        .id(patient.getId())
        .professionalId(patient.getProfessionalId())
        .patientStatusId(patient.getStatus() != null ? patient.getStatus().getId() : null)
        .genderId(patient.getGender() != null ? patient.getGender().getId() : null)
        .educationLevelId(
            patient.getEducationLevel() != null ? patient.getEducationLevel().getId() : null)
        .ethnicityId(patient.getEthnicity() != null ? patient.getEthnicity().getId() : null)
        .firstName(patient.getFirstName())
        .lastName(patient.getLastName())
        .socialName(patient.getSocialName())
        .contactEmail(patient.getContactEmail())
        .mobilePhone(patient.getMobilePhone())
        .landlinePhone(patient.getLandlinePhone())
        .cpf(patient.getCpf())
        .rg(patient.getRg())
        .birthDate(patient.getBirthDate())
        .placeOfBirth(patient.getPlaceOfBirth())
        .occupation(patient.getOccupation())
        .howTheyFoundUs(patient.getHowTheyFoundUs())
        .referredBy(patient.getReferredBy())
        .identificationColor(patient.getIdentificationColor())
        .notes(patient.getNotes())
        .address(toAddressDto(patient.getAddress()))
        .guardian(toGuardianDto(patient.getGuardian()))
        .emergencyContacts(toEmergencyContactDtos(patient.getEmergencyContacts()))
        .tagIds(toTagIds(patient.getPatientTags()))
        .build();
  }

  public Patient toEntity(PatientDto dto) {
    if (dto == null) {
      return null;
    }

    Patient patient =
        Patient.builder()
            .id(dto.getId())
            .professionalId(dto.getProfessionalId())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .socialName(dto.getSocialName())
            .contactEmail(dto.getContactEmail())
            .mobilePhone(dto.getMobilePhone())
            .landlinePhone(dto.getLandlinePhone())
            .cpf(dto.getCpf())
            .rg(dto.getRg())
            .birthDate(dto.getBirthDate())
            .placeOfBirth(dto.getPlaceOfBirth())
            .occupation(dto.getOccupation())
            .howTheyFoundUs(dto.getHowTheyFoundUs())
            .referredBy(dto.getReferredBy())
            .identificationColor(dto.getIdentificationColor())
            .notes(dto.getNotes())
            .build();

    if (dto.getAddress() != null) {
      patient.setAddress(toAddressEntity(dto.getAddress(), patient));
    }
    if (dto.getGuardian() != null) {
      patient.setGuardian(toGuardianEntity(dto.getGuardian(), patient));
    }
    if (dto.getEmergencyContacts() != null) {
      patient.setEmergencyContacts(
          dto.getEmergencyContacts().stream()
              .map(e -> toEmergencyContactEntity(e, patient))
              .toList());
    }

    return patient;
  }

  private PatientAddressDto toAddressDto(PatientAddress address) {
    if (address == null) {
      return null;
    }
    return PatientAddressDto.builder()
        .id(address.getId())
        .country(address.getCountry())
        .zipCode(address.getZipCode())
        .state(address.getState())
        .city(address.getCity())
        .neighborhood(address.getNeighborhood())
        .street(address.getStreet())
        .number(address.getNumber())
        .complement(address.getComplement())
        .build();
  }

  private PatientAddress toAddressEntity(PatientAddressDto dto, Patient patient) {
    return PatientAddress.builder()
        .id(dto.getId())
        .country(dto.getCountry())
        .zipCode(dto.getZipCode())
        .state(dto.getState())
        .city(dto.getCity())
        .neighborhood(dto.getNeighborhood())
        .street(dto.getStreet())
        .number(dto.getNumber())
        .complement(dto.getComplement())
        .patient(patient)
        .build();
  }

  private PatientGuardianDto toGuardianDto(PatientGuardian guardian) {
    if (guardian == null) {
      return null;
    }
    return PatientGuardianDto.builder()
        .id(guardian.getId())
        .guardianName(guardian.getGuardianName())
        .guardianEmail(guardian.getGuardianEmail())
        .guardianMobilePhone(guardian.getGuardianMobilePhone())
        .guardianCpf(guardian.getGuardianCpf())
        .guardianRg(guardian.getGuardianRg())
        .guardianBirthDate(guardian.getGuardianBirthDate())
        .allowDocumentBilling(guardian.isAllowDocumentBilling())
        .allowSessionReminders(guardian.isAllowSessionReminders())
        .build();
  }

  private PatientGuardian toGuardianEntity(PatientGuardianDto dto, Patient patient) {
    return PatientGuardian.builder()
        .id(dto.getId())
        .guardianName(dto.getGuardianName())
        .guardianEmail(dto.getGuardianEmail())
        .guardianMobilePhone(dto.getGuardianMobilePhone())
        .guardianCpf(dto.getGuardianCpf())
        .guardianRg(dto.getGuardianRg())
        .guardianBirthDate(dto.getGuardianBirthDate())
        .allowDocumentBilling(dto.isAllowDocumentBilling())
        .allowSessionReminders(dto.isAllowSessionReminders())
        .patient(patient)
        .build();
  }

  private List<EmergencyContactDto> toEmergencyContactDtos(List<EmergencyContact> contacts) {
    if (contacts == null) {
      return Collections.emptyList();
    }
    return contacts.stream()
        .map(
            c ->
                EmergencyContactDto.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .relationship(c.getRelationship())
                    .phone(c.getPhone())
                    .isPrimary(c.isPrimary())
                    .build())
        .toList();
  }

  private EmergencyContact toEmergencyContactEntity(EmergencyContactDto dto, Patient patient) {
    return EmergencyContact.builder()
        .id(dto.getId())
        .name(dto.getName())
        .relationship(dto.getRelationship())
        .phone(dto.getPhone())
        .isPrimary(dto.isPrimary())
        .patient(patient)
        .build();
  }

  private List<Integer> toTagIds(List<PatientTag> patientTags) {
    if (patientTags == null) {
      return Collections.emptyList();
    }
    return patientTags.stream().map(pt -> pt.getTag().getId()).toList();
  }
}
