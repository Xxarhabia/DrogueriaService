package com.msara.servicio.controllers.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"message", "date"})
public record ProductCreateResponse(String message, LocalDateTime date) {
}
