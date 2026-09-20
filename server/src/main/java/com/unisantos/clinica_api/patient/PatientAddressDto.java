package com.unisantos.clinica_api.patient;

public class PatientAddressDto {

  private Integer id;
  private byte[] country;
  private byte[] zipCode;
  private byte[] state;
  private byte[] city;
  private byte[] neighborhood;
  private byte[] street;
  private byte[] number;
  private byte[] complement;

  public PatientAddressDto() {}

  public PatientAddressDto(
      Integer id,
      byte[] country,
      byte[] zipCode,
      byte[] state,
      byte[] city,
      byte[] neighborhood,
      byte[] street,
      byte[] number,
      byte[] complement) {
    this.id = id;
    this.country = country;
    this.zipCode = zipCode;
    this.state = state;
    this.city = city;
    this.neighborhood = neighborhood;
    this.street = street;
    this.number = number;
    this.complement = complement;
  }

  public static PatientAddressDtoBuilder builder() {
    return new PatientAddressDtoBuilder();
  }

  public static class PatientAddressDtoBuilder {
    private Integer id;
    private byte[] country;
    private byte[] zipCode;
    private byte[] state;
    private byte[] city;
    private byte[] neighborhood;
    private byte[] street;
    private byte[] number;
    private byte[] complement;

    public PatientAddressDtoBuilder id(Integer id) {
      this.id = id;
      return this;
    }

    public PatientAddressDtoBuilder country(byte[] country) {
      this.country = country;
      return this;
    }

    public PatientAddressDtoBuilder zipCode(byte[] zipCode) {
      this.zipCode = zipCode;
      return this;
    }

    public PatientAddressDtoBuilder state(byte[] state) {
      this.state = state;
      return this;
    }

    public PatientAddressDtoBuilder city(byte[] city) {
      this.city = city;
      return this;
    }

    public PatientAddressDtoBuilder neighborhood(byte[] neighborhood) {
      this.neighborhood = neighborhood;
      return this;
    }

    public PatientAddressDtoBuilder street(byte[] street) {
      this.street = street;
      return this;
    }

    public PatientAddressDtoBuilder number(byte[] number) {
      this.number = number;
      return this;
    }

    public PatientAddressDtoBuilder complement(byte[] complement) {
      this.complement = complement;
      return this;
    }

    public PatientAddressDto build() {
      return new PatientAddressDto(
          id, country, zipCode, state, city, neighborhood, street, number, complement);
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
