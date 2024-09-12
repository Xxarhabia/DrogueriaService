package com.msara.servicio.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthRegisterRequest(@NotBlank String name,
                                  @NotBlank String email,
                                  @NotBlank String password,
                                  @NotBlank String confirmPassword,
                                  @NotBlank String address,
                                  @Valid AuthRegisterRoleRequest roleRequest) {
}
