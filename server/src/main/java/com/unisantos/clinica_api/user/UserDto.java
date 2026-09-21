package com.unisantos.clinica_api.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Long id;

  private String firstName;

  private String lastName;

  private String loginEmail;

  @NotNull private Integer userTypeId;

  private String userType;

  private Boolean isActive;
}
