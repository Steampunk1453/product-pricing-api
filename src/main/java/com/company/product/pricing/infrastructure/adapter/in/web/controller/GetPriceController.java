package com.company.product.pricing.infrastructure.adapter.in.web.controller;

import com.company.product.pricing.application.PriceService;
import com.company.product.pricing.infrastructure.adapter.in.web.dto.ErrorResponseDto;
import com.company.product.pricing.infrastructure.adapter.in.web.dto.PriceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
@Validated
@Tag(name = "Prices", description = "Endpoint for querying product prices")
public class GetPriceController {

    private final PriceService priceService;

    public GetPriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    @Operation(
        summary = "Query applicable price",
        description = "Fetches the applicable rate and price for an effective date, product and brand"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Price successfully found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or missing query parameters",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No applicable price rate found for the given request",
            content = @Content
        )
    })
    public ResponseEntity<PriceResponseDto> getPrice(
            @Parameter(description = "Price effective date", example = "2020-06-14T10:00:00")
            @RequestParam("effectiveDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime effectiveDate,

            @Parameter(description = "Product unique identifier", example = "35455")
            @RequestParam("productId") Integer productId,

            @Parameter(description = "Brand unique identifier (1 = ZARA)", example = "1")
            @RequestParam("brandId") Integer brandId) {

        return priceService.findPrice(effectiveDate, productId, brandId)
                .map(PriceResponseDto::fromDomain)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
