package com.msara.servicio.controllers.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message", "product_name", "quantity"})
public record CartAddItemResponse(String message, String productName, int quantity) {
}
