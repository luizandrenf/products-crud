package com.example.crud.controllers;


import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import com.example.crud.domain.product.RequestProduct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping
    public ResponseEntity getAllProducts(){
        var allProduts = repository.findAllByActiveTrue();
        return ResponseEntity.ok(allProduts);
    }

    @PostMapping
    public ResponseEntity registerProduct(@RequestBody @Validated RequestProduct data){
        Product newProduct = new Product(data);
        repository.save(newProduct);
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateProduct(@RequestBody @Validated RequestProduct data){
        Optional<Product> optionalProduct = repository.findById(data.id());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optionalProduct.get();
        product.setName(data.name());
        product.setPrice_in_cents(data.price_in_cents());
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteProduct(@PathVariable String id){
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optionalProduct.get();
        product.setActive(false);
        return ResponseEntity.noContent().build();
    }

}
