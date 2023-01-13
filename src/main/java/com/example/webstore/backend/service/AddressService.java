package com.example.webstore.backend.service;

import com.example.webstore.backend.api.AddressSaveRequest;
import com.example.webstore.backend.model.Address;
import com.example.webstore.backend.model.dto.AddressDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressDAO addressDAO;

    @Autowired
    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public Address addAddress (AddressSaveRequest request) {
        Address address = new Address();
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());

        address = addressDAO.save(address);
        return address;
    }

    public List<Address> getAddress() {
        return addressDAO.findAll();
    }

    public Optional<Address> findById(Long id) {
        return addressDAO.findById(id);
    }

    public void deleteById(Long id) {
        addressDAO.deleteById(id);
    }


}
