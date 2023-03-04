package com.example.webstore.backend.runner;

import com.example.webstore.backend.model.dto.AddressDAO;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import com.example.webstore.backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AddressRunner implements CommandLineRunner {

    private final AddressService addressService;
    private final LocalUserDAO localUserDAO;
    private final AddressDAO addressDAO;


    @Autowired
    public AddressRunner(AddressService addressService,
                         LocalUserDAO localUserDAO, AddressDAO addressDAO) {
        this.addressService = addressService;
        this.localUserDAO = localUserDAO;
        this.addressDAO = addressDAO;

    }

    @Override
    public void run(String... args) throws Exception {

//
//        LocalUser user = new LocalUser();
//        user.setFirstName("Fathers Object Runner_TEST");
//        user.setLastName("Runner_TEST1");
//        user.setEmail("Testa@abv.bg");
//        user.setPassword("87777");
//        user.setUsername("userTss");
//        localUserDAO.save(user);
//
//        Address address = new Address();
//        address.setAddressLine1("Child Object Address_Test1");
//        address.setAddressLine2("Test1");
//        address.setCity("PL1");
//        address.setCountry("BG1");
//        address.setUser(user);
//
//        addressDAO.save(address);




    }
}