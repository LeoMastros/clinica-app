package com.unisantos.clinica_api.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Patient_Address")
public class PatientAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_address")
  private Integer id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_patient", unique = true)
  private Patient patient;

  @Column(name = "country", columnDefinition = "VARBINARY(60)")
  private byte[] country;

  @Column(name = "zip_code", columnDefinition = "VARBINARY(12)")
  private byte[] zipCode;

  @Column(name = "state", columnDefinition = "VARBINARY(60)")
  private byte[] state;

  @Column(name = "city", columnDefinition = "VARBINARY(80)")
  private byte[] city;

  @Column(name = "neighborhood", columnDefinition = "VARBINARY(80)")
  private byte[] neighborhood;

  @Column(name = "street", columnDefinition = "VARBINARY(150)")
  private byte[] street;

  @Column(name = "number", columnDefinition = "VARBINARY(20)")
  private byte[] number;

  @Column(name = "complement", columnDefinition = "VARBINARY(80)")
  private byte[] complement;

  public PatientAddress() {}

  public PatientAddress(
      Integer id,
      Patient patient,
      byte[] country,
      byte[] zipCode,
      byte[] state,
      byte[] city,
      byte[] neighborhood,
      byte[] street,
      byte[] number,
      byte[] complement) {
    this.id = id;
    this.patient = patient;
    this.country = country;
    this.zipCode = zipCode;
    this.state = state;
    this.city = city;
    this.neighborhood = neighborhood;
    this.street = street;
    this.number = number;
    this.complement = complement;
  }

  public static PatientAddressBuilder builder() {
    return new PatientAddressBuilder();
  }

  public static class PatientAddressBuilder {
    private Integer id;
    private Patient patient;
    private byte[] country;
    private byte[] zipCode;
    private byte[] state;
    private byte[] city;
    private byte[] neighborhood;
    private byte[] street;
    private byte[] number;
    private byte[] complement;

    public PatientAddressBuilder id(Integer id) {
      this.id = id;
      return this;
    }

    public PatientAddressBuilder patient(Patient patient) {
      this.patient = patient;
      return this;
    }

    public PatientAddressBuilder country(byte[] country) {
      this.country = country;
      return this;
    }

    public PatientAddressBuilder zipCode(byte[] zipCode) {
      this.zipCode = zipCode;
      return this;
    }

    public PatientAddressBuilder state(byte[] state) {
      this.state = state;
      return this;
    }

    public PatientAddressBuilder city(byte[] city) {
      this.city = city;
      return this;
    }

    public PatientAddressBuilder neighborhood(byte[] neighborhood) {
      this.neighborhood = neighborhood;
      return this;
    }

    public PatientAddressBuilder street(byte[] street) {
      this.street = street;
      return this;
    }

    public PatientAddressBuilder number(byte[] number) {
      this.number = number;
      return this;
    }

    public PatientAddressBuilder complement(byte[] complement) {
      this.complement = complement;
      return this;
    }

    public PatientAddress build() {
      return new PatientAddress(
          id, patient, country, zipCode, state, city, neighborhood, street, number, complement);
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public byte[] getCountry() {
    return country;
  }

  public void setCountry(byte[] country) {
    this.country = country;
  }

  public byte[] getZipCode() {
    return zipCode;
  }

  public void setZipCode(byte[] zipCode) {
    this.zipCode = zipCode;
  }

  public byte[] getState() {
    return state;
  }

  public void setState(byte[] state) {
    this.state = state;
  }

  public byte[] getCity() {
    return city;
  }

  public void setCity(byte[] city) {
    this.city = city;
  }

  public byte[] getNeighborhood() {
    return neighborhood;
  }

  public void setNeighborhood(byte[] neighborhood) {
    this.neighborhood = neighborhood;
  }

  public byte[] getStreet() {
    return street;
  }

  public void setStreet(byte[] street) {
    this.street = street;
  }

  public byte[] getNumber() {
    return number;
  }

  public void setNumber(byte[] number) {
    this.number = number;
  }

  public byte[] getComplement() {
    return complement;
  }

  public void setComplement(byte[] complement) {
    this.complement = complement;
  }
}
