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
import java.util.NoSuchElementException;
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



    private boolean userHasPermission(LocalUser user, Long id) {
        return user.getId() == id;
    }


    @GetMapping(value = "/{userId}/address")
    public ResponseEntity<List<Address>> getAddressesByLoggedUser(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {

        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Address> userAddress = addressDAO.findByUser_Id(userId);

        return new ResponseEntity<>(userAddress, HttpStatus.OK);
    }



    @PutMapping(value = "/{userId}/address")
    public ResponseEntity<Address> putAddressToUser(@AuthenticationPrincipal LocalUser user,
                                                 @PathVariable Long userId,
                                                 @RequestBody Address addressToUpdate) {

        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        addressToUpdate.setId(null);

        LocalUser fakeUser = new LocalUser();

        fakeUser.setId(userId);

        addressToUpdate.setUser(fakeUser);

        Address updatedAddress = addressDAO.save(addressToUpdate);

        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }


    @PatchMapping(value = "/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(@AuthenticationPrincipal LocalUser user,
                                                @PathVariable Long userId,
                                                @PathVariable Long addressId,
                                                @RequestBody Address addressToPatch) {

        if (!userHasPermission(user, userId)) { // проверка дали AuthenticationPrincipal LocalUser user има права за промяна
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (addressToPatch.getId() == (addressId)) { // адреса за промяна е == на адреса в Url
            Optional<Address> originalAddress = addressDAO.findById(addressId); // вземи оригиналния адрес ако съществува

            if (originalAddress.isPresent()) { // ако оригиналния адрес съществува - вземи му originalUser
                LocalUser originalUser = originalAddress.get().getUser(); //

                if (originalUser.getId() == userId) {
                    addressToPatch.setUser(originalUser);

                    Address patchedAddress = addressDAO.save(addressToPatch);

                   return new ResponseEntity<>(patchedAddress, HttpStatus.OK);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



    @GetMapping
    public ResponseEntity <List<LocalUser>> getUsers() {

        List<LocalUser> byUsernameIgnoreCase = userDAO.findAll();

        return new ResponseEntity<>(byUsernameIgnoreCase, HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            userDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new NoSuchElementException("No such User");
        }
    }

}


