package com.msara.servicio.controllers.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CartRequest(@NotNull Long productId, @NotNull int quantity) {
}
