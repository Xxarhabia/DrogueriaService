package com.msara.servicio.controllers.dto.request;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String email, @NotBlank String password) {
}
