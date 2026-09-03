package com.company.product.pricing.domain.port;

import com.company.product.pricing.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findPriceByPriority(LocalDateTime effectiveDate, Integer productId, Integer brandId);
}
