package com.fiap.produto.service;

import com.fiap.produto.domain.Category;
import com.fiap.produto.domain.Product;
import com.fiap.produto.repository.CategoryRepository;
import com.fiap.produto.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {
    private List<Product> products = new ArrayList<>();

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CatalogService(final ProductRepository productRepository,
                          final CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Product add(final String name, final String description) {
        final Product product = new Product()
                .setName(name)
                .setDescription(description)
                .setCategory(getDefaultCategory());
        products.add(product);
        productRepository.save(product);
        return product;
    }

    private Category getDefaultCategory() {
        final String categoryName = "Padrão";

        final Optional<Category> categoryDefault = categoryRepository.findByName(categoryName);

        return categoryDefault.orElseGet(() ->
                categoryRepository.save(new Category()
                        .setName(categoryName)
                        .setDescription("Categoria padrão")));
    }

    public Page<Product> listAll() {
        return productRepository.findAll(PageRequest.of(0,20));
    }

    public Optional<Product> findBy(final long id) {
        return productRepository.findById(id);
    }
}
