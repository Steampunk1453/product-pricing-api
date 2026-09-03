package com.company.product.pricing.infrastructure.web.response;

import com.company.product.pricing.domain.model.Price;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Response containing details of the price rate")
public record PriceResponse(
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
    BigDecimal price,
    @Schema(description = "ISO 4217 currency code", example = "EUR")
    String currency
) {
    public static PriceResponse fromDomain(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price(),
                price.currency()
        );
    }
}
