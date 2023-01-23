package com.example.webstore.backend.service;

import com.example.webstore.backend.api.model.UpdateProductRequest;
import com.example.webstore.backend.api.model.UpdateProductResponse;
import com.example.webstore.backend.model.Product;
import com.example.webstore.backend.model.dto.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProducts() {
        return productDAO.findAll();
    }


    public Product update(Product product, UpdateProductRequest request) {

        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setLongDescription(request.getLongDescription());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());

        return product;

    }

    public Product update(Long id, UpdateProductRequest request) {

        Product product = productDAO.findProductById(id);

        Product updatedProduct = update(product, request);

        productDAO.save(updatedProduct);

        return updatedProduct;
    }

    public UpdateProductResponse converter(Product product) {

        UpdateProductResponse response = new UpdateProductResponse();
        response.setName(product.getName());
        response.setShortDescription(product.getShortDescription());
        response.setLongDescription(product.getLongDescription());
        response.setPrice(product.getPrice());
        response.setInventory(product.getInventory());
        return response;
    }
}