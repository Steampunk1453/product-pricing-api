package com.company.product.pricing.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard error response structure")
public record ErrorResponseDto(
    @Schema(description = "Timestamp of the error occurrence", example = "2024-03-01T10:00:00")
    LocalDateTime timestamp,

    @Schema(description = "HTTP status code", example = "400")
    int status,

    @Schema(description = "HTTP error title", example = "Bad Request")
    String error,

    @Schema(description = "Detailed error message", example = "Parameter 'productId' is required.")
    String message
) {}
