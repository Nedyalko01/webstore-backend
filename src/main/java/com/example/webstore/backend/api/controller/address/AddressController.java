package com.example.webstore.backend.api.controller.address;

import com.example.webstore.backend.model.Address;
import com.example.webstore.backend.model.dto.AddressDAO;
import com.example.webstore.backend.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final AddressDAO addressDAO;

    public AddressController(AddressService addressService,
                             AddressDAO addressDAO) {
        this.addressService = addressService;
        this.addressDAO = addressDAO;

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<Address>> findById(@PathVariable Long id) {

        Optional<Address> find = addressDAO.findById(id);

        return new ResponseEntity<>(find, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            addressDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new NoSuchElementException("No such Address");
        }
    }



    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAddress();
    }


}