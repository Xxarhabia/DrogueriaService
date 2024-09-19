package com.msara.servicio.controllers.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"transaction", "message", "status",  "date"})
public record TransactionResponse(String transaction, String message, boolean status, String date) {
}
