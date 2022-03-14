package com.fiap.produto.controller;

public class ProductAddRequest {
    private String name;
    private String description;

    public ProductAddRequest() {
    }

    public String getName() {
        return name;
    }

    public ProductAddRequest setName(final String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductAddRequest setDescription(final String description) {
        this.description = description;
        return this;
    }
}