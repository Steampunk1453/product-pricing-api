package com.company.product.pricing.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query(value = """
        SELECT *
        FROM PRICES
        WHERE BRAND_ID = :brandId
          AND PRODUCT_ID = :productId
          AND :effectiveDate BETWEEN START_DATE AND END_DATE
        ORDER BY PRIORITY DESC
        LIMIT 1
        """, nativeQuery = true)
    Optional<PriceEntity> findPriceByPriority(
        @Param("effectiveDate") LocalDateTime effectiveDate,
        @Param("productId") Integer productId,
        @Param("brandId") Integer brandId
    );

}
