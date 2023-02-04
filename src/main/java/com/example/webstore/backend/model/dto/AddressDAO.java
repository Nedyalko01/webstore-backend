package com.example.webstore.backend.model.dto;

import com.example.webstore.backend.model.Address;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface AddressDAO extends ListCrudRepository<Address, Long> {

    Optional<Address> findByUser_Id(Long id);

    @Override
    List<Address> findAll();

    Optional<Address> findById(Long id);

    void deleteById(Long id);
}
