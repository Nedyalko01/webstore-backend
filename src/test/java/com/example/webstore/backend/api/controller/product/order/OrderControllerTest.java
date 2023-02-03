package com.example.webstore.backend.api.controller.product.order;

import com.example.webstore.backend.model.WebOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @Transactional
    public void testUserAAuthenticatedOrderList() throws Exception {
        userAuthenticatedOrderListTest("UserA");

    }

    @Test
    public void userAuthenticatedOrderListTest(String username)  throws Exception {
        mvc.perform(get("/order")).andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<WebOrder> orders = new ObjectMapper().readValue(json, new TypeReference<List<WebOrder>>() {});
                    for (WebOrder order : orders) {
                        Assertions.assertEquals(username, order.getUser().getUsername(), "Order list should only be orders belonging to the user.");
                    }
                });
    }

    @Test
    public void unauthenticatedOrderListTest() throws Exception {
    mvc.perform(get("/order")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));

    }
}
