package com.example.webstore.backend.service;

import com.example.webstore.backend.model.Product;
import com.example.webstore.backend.model.dto.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProduct() {
        return productDAO.findAll();
    }
}
