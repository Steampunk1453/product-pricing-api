package com.company.product.pricing.infrastructure.adapter.in.web.dto;

import com.company.product.pricing.domain.Price;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Response containing details of the price rate")
public record PriceResponseDto(
    @Schema(description = "Product unique identifier", example = "35455")
    Integer productId,

    @Schema(description = "Brand unique identifier", example = "1")
    Integer brandId,

    @Schema(description = "Applicable rate", example = "2")
    Integer applicableRate,

    @Schema(description = "Start date of the rate application", example = "2020-06-14T15:00:00")
    LocalDateTime startDate,

    @Schema(description = "End date of the rate application", example = "2020-06-14T18:30:00")
    LocalDateTime endDate,

    @Schema(description = "Final price", example = "25.45")
    BigDecimal price
) {
    public static PriceResponseDto fromDomain(Price price) {
        return new PriceResponseDto(
            price.productId(),
            price.brandId(),
            price.priceList(),
            price.startDate(),
            price.endDate(),
            price.price()
        );
    }
}
