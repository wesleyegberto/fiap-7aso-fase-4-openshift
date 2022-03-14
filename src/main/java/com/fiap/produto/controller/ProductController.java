package com.fiap.produto.controller;

import com.fiap.produto.domain.Product;
import com.fiap.produto.service.CatalogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CatalogService catalogService;

    public ProductController(final CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // GET /products --> listar produtos
    @GetMapping
    public ResponseEntity<Page<Product>> list() {
        return ResponseEntity.ok(
                this.catalogService.listAll()
        );
    }

    // GET /products/{id} --> detalhes de um produto
    @GetMapping(
            path = "{id}"
    )
    public ResponseEntity<Product> details(@PathVariable("id") long id) {
        final Optional<Product> by = this.catalogService.findBy(id);
        if (by.isPresent()) {
            return ResponseEntity.ok(by.get());
        }

        return ResponseEntity.notFound().build();
    }

    // POST /products { ... } --> adiciona um novo produto
    @PostMapping()
    public ResponseEntity<Product> add(@RequestBody Product product) throws URISyntaxException {

        final Product add = this.catalogService.add(product.getName(), product.getDescription());

        return ResponseEntity.created(new URI("/products/" + add.getId())).build();
    }

}
