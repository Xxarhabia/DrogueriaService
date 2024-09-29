package com.msara.servicio.controllers.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequest(@NotBlank @NotNull String name,
                                   @NotBlank @NotNull String reference,
                                   @NotNull double amount,
                                   @NotNull int stock,
                                   @NotBlank @NotNull String category) {
}
