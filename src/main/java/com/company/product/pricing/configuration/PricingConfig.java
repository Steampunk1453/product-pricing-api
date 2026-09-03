package com.company.product.pricing.configuration;

import com.company.product.pricing.application.usecase.GetPrice;
import com.company.product.pricing.domain.port.PriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PricingConfig {

    @Bean
    GetPrice getPrice(PriceRepository priceRepository) {
        return new GetPrice(priceRepository);
    }
}
