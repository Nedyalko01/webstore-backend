package com.example.webstore.backend.model.dto;

import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long> {

    List<WebOrder> findByUser(LocalUser user);
}
