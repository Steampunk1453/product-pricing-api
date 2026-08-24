package com.company.product.pricing.application;

import com.company.product.pricing.domain.Price;
import com.company.product.pricing.infrastructure.repository.PriceEntity;
import com.company.product.pricing.infrastructure.repository.JpaPriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PriceService {

    private final JpaPriceRepository priceRepository;

    public PriceService(JpaPriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Optional<Price> findPrice(LocalDateTime effectiveDate, Integer productId, Integer brandId) {
        return priceRepository.findPriceByPriority(effectiveDate, productId, brandId)
                .map(this::toDomain);
    }

    private Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getId(),
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }

}
