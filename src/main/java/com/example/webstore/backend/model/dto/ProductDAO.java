package com.example.webstore.backend.model.dto;

import com.example.webstore.backend.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {

    Product findProductById(Long id);
}
