package com.amrut.prabhu.web;

import com.amrut.prabhu.exception.ProductNotFound;
import com.amrut.prabhu.model.Product;
import com.amrut.prabhu.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
public class WebController {

    private ProductRepository productRepository;

    public WebController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/product")
    public ResponseEntity create(@RequestBody Product product) {
        product = productRepository.save(product);
        return ResponseEntity.created(URI.create("/product/" + product.getId()))
                .body(product);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity getProduct(@PathVariable("id") String id) {
        Optional<Product> productById = productRepository.findById(id);
        return productById.map(ResponseEntity::ok)
                .orElseThrow(ProductNotFound::new);
    }
}
