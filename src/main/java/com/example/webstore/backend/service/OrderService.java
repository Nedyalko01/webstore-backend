package com.example.webstore.backend.service;

import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.WebOrder;
import com.example.webstore.backend.model.dto.WebOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final WebOrderDAO webOrderDAO;

    public OrderService(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    public List<WebOrder> getOrders(LocalUser user) {
        return webOrderDAO.findByUser(user);
    }
}
