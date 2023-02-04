package com.example.webstore.backend.api.controller.user;

import com.example.webstore.backend.model.Address;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.AddressDAO;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AddressDAO addressDAO;

    private final LocalUserDAO userDAO;

    @Autowired
    public UserController(AddressDAO addressDAO, LocalUserDAO userDAO) {
        this.addressDAO = addressDAO;
        this.userDAO = userDAO;
    }


    @GetMapping(value = "/{id}/address")
    public ResponseEntity<Optional<Address>> getAddressByUser(@AuthenticationPrincipal LocalUser user, @PathVariable Long id) {

        if (!userHasPermission(user, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Address> userAddress = addressDAO.findById(id);

        return new ResponseEntity<>(userAddress, HttpStatus.OK);
    }

    private boolean userHasPermission(LocalUser user, Long id) {
        return user.getId() == id;
    }

    @PutMapping(value = "/{id}/address")
    public ResponseEntity<Address> updateAddress(@AuthenticationPrincipal LocalUser user,
                                                 @PathVariable Long id,
                                                 @RequestBody Address address) {

        if (!userHasPermission(user, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setId(null);

        LocalUser refUser = new LocalUser();
        refUser.setId(id);
        address.setUser(refUser);

        Address updatedAddress = addressDAO.save(address);

        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(@AuthenticationPrincipal LocalUser user,
                                                @PathVariable Long id,
                                                @PathVariable Long addressId,
                                                @RequestBody Address address) {

        if (!userHasPermission(user, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (address.getId() == (addressId)) {
            Optional<Address> originalAddress = addressDAO.findById(addressId);

            if (originalAddress.isPresent()) {
                LocalUser originalUser = originalAddress.get().getUser();


                if (originalUser.getId() == id) {
                    address.setUser(originalUser);

                    Address patchedAddress = addressDAO.save(address);

                   return new ResponseEntity<>(patchedAddress, HttpStatus.OK);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping
    public ResponseEntity <List<LocalUser>> getUser() {

        List<LocalUser> byUsernameIgnoreCase = userDAO.findAll();

        return new ResponseEntity<>(byUsernameIgnoreCase, HttpStatus.OK);

    }
}


