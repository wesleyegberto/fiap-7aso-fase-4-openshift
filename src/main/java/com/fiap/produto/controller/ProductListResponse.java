package com.fiap.produto.controller;

import com.fiap.produto.domain.Product;

public class ProductListResponse {

    private final long id;
    private final String name;
    private final String description;

    public ProductListResponse(final Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
