package com.example.webstore.backend.api.controller.products;

import com.example.webstore.backend.model.Product;
import com.example.webstore.backend.model.dto.ProductDAO;
import com.example.webstore.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductsController {

    private final ProductService productService;

    private final ProductDAO productDAO;

    public ProductsController(ProductService productService, ProductDAO productDAO) {
        this.productService = productService;
        this.productDAO = productDAO;
    }


    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product productRequest) {
        Product product = productService.addProduct(productRequest);

        return new ResponseEntity<>(product, HttpStatus.OK);
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
