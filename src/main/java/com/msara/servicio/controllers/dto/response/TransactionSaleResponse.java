package com.msara.servicio.controllers.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"transaction", "message", "date"})
public record TransactionSaleResponse(String transaction, String message, String date) {
}
