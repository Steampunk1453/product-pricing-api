package com.company.product.pricing.infrastructure.web;

import com.company.product.pricing.infrastructure.web.response.ErrorResponse;
import com.company.product.pricing.infrastructure.web.response.PriceResponse;
import com.company.product.pricing.application.usecase.GetPrice;
import com.company.product.pricing.domain.model.PriceNotFoundException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
@Validated
@Tag(name = "Prices", description = "Endpoint for querying product prices")
public class GetPriceController {

    private final GetPrice getPrice;

    public GetPriceController(GetPrice getPrice) {
        this.getPrice = getPrice;
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
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PriceResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or missing query parameters",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No applicable price rate found for the given request",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<PriceResponse> getPrice(
            @Parameter(description = "Price effective date", example = "2020-06-14T10:00:00")
            @RequestParam("effectiveDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime effectiveDate,
            @Parameter(description = "Product unique identifier", example = "35455")
            @RequestParam("productId") Integer productId,
            @Parameter(description = "Brand unique identifier (1 = ZARA)", example = "1")
            @RequestParam("brandId") Integer brandId) {

        return getPrice.execute(effectiveDate, productId, brandId)
                .map(PriceResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElseThrow(PriceNotFoundException::new);
    }
}
