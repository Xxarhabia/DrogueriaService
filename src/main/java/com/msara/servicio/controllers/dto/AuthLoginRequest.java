package com.msara.servicio.controllers.dto;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String email, @NotBlank String password) {
}
