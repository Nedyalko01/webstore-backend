package com.example.webstore.backend.api.controller.products;

import com.example.webstore.backend.model.Address;
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


    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) {

        Product product = productService.findOneById(id);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

}
