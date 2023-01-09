package com.example.webstore.backend.runner;

import com.example.webstore.backend.model.Address;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.AddressDAO;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import com.example.webstore.backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
//public class AddressRunner implements CommandLineRunner {

//    private final AddressService addressService;
//    private final LocalUserDAO localUserDAO;
//
//    private final AddressDAO addressDAO;
//
//    @Autowired
//    public AddressRunner(AddressService addressService,
//                         LocalUserDAO localUserDAO, AddressDAO addressDAO) {
//        this.addressService = addressService;
//        this.localUserDAO = localUserDAO;
//        this.addressDAO = addressDAO;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        LocalUser user = new LocalUser();
//        user.setFirstName("Ivan");
//        user.setLastName("Petov");
//        user.setEmail("hhddd@abv.bg");
//        user.setPassword("87777");
//        user.setUsername("user666s");
//        localUserDAO.save(user);
//
//
//        Address address = new Address();
//        address.setAddressLine1("123 New Yard");
//        address.setAddressLine2("55 Last Hampshire");
//        address.setCity("Dallas");
//        address.setCountry("USA");
//        address.setUser(user);
//
//        addressDAO.save(address);
//
//
//        LocalUser userTwo = new LocalUser();
//        userTwo .setFirstName("USER2");
//        userTwo .setLastName("Petov2");
//        userTwo .setEmail("hhAS@gmail.bg");
//        userTwo .setPassword("87777hhh");
//        userTwo .setUsername("user0");
//        localUserDAO.save(userTwo);
//
//
//        Address addressTWO = new Address();
//        addressTWO.setAddressLine1("123 LA");
//        addressTWO.setAddressLine2("77 Hamptons");
//        addressTWO.setCity("Boston");
//        addressTWO.setCountry("USA");
//        addressTWO.setUser(userTwo);
//
//        addressDAO.save(addressTWO);

//    }
//}
