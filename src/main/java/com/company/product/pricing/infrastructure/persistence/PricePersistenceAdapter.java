package com.company.product.pricing.infrastructure.persistence;

import com.company.product.pricing.domain.model.Price;
import com.company.product.pricing.domain.port.PriceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PricePersistenceAdapter implements PriceRepository {

    private final PriceJpaRepository repository;

    public PricePersistenceAdapter(PriceJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Price> findPriceByPriority(
            LocalDateTime effectiveDate,
            Integer productId,
            Integer brandId) {
        return repository.findPriceByPriority(effectiveDate, productId, brandId)
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
