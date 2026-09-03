package com.company.product.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(
    Long id,
    Integer brandId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Integer priceList,
    Integer productId,
    Integer priority,
    BigDecimal price,
    String currency
) {}
