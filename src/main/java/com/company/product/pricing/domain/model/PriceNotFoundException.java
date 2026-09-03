package com.company.product.pricing.domain.model;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException() {
        super("No applicable price rate found for the given request.");
    }
}
