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

    public List<Product> getProducts() {
        return productDAO.findAll();
    }

    public Product findOneById(Long id) {
      return  productDAO.findAll()
              .stream()
              .filter(product -> product.getId() == id)
              .findFirst().orElseThrow(()  -> new RuntimeException("Product not found"));
    }

    public Product addProduct(Product product) {
        return productDAO.save(product);
    }

}
