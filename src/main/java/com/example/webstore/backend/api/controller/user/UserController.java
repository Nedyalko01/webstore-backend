package com.example.webstore.backend.api.controller.user;

import com.example.webstore.backend.model.Address;
import com.example.webstore.backend.model.dto.AddressDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AddressDAO addressDAO;

    @Autowired
    public UserController(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    @GetMapping(value = "/{id}/address")
    public ResponseEntity<List<Address>> getAddress(@PathVariable Long id) {

        List<Address> user = addressDAO.findByUser_Id(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}


