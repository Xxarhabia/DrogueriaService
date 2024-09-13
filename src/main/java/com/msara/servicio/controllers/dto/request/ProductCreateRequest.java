package com.msara.servicio.controllers.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequest(@NotBlank String name,
                                   @NotBlank String reference,
                                   @NotNull double amount,
                                   @NotNull int stock,
                                   @NotBlank String category) {
}
