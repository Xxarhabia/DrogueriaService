package com.msara.servicio.controllers.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"message", "date", "status"})
public record ProductResponse(String message, LocalDateTime date, boolean status) {
}
