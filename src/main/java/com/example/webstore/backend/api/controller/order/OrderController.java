package com.example.webstore.backend.api.controller.order;

import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.WebOrder;
import com.example.webstore.backend.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<WebOrder> getOrdersFromUser(@AuthenticationPrincipal LocalUser user) {
        return orderService.getOrders(user);
    }
}
