package com.example.webstore.backend.api.controller.products;

import com.example.webstore.backend.api.model.UpdateProductRequest;
import com.example.webstore.backend.api.model.UpdateProductResponse;
import com.example.webstore.backend.model.Product;
import com.example.webstore.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getProducts();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable Long id, UpdateProductRequest request) {

        Product product = productService.update(id, request);

        UpdateProductResponse productResponse = productService.converter(product);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);



    }
}
