package com.company.product.pricing.application.usecase;

import com.company.product.pricing.domain.model.Price;
import com.company.product.pricing.domain.port.PriceRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public record GetPrice(PriceRepository repository) {

    public Optional<Price> execute(
            LocalDateTime effectiveDate,
            Integer productId,
            Integer brandId) {
        return repository.findPriceByPriority(effectiveDate, productId, brandId);
    }
}
