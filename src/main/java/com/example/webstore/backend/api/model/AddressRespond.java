package com.example.webstore.backend.api.model;

import com.example.webstore.backend.model.LocalUser;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class AddressRespond {

    private Long id;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String country;

    private LocalUser user;


}
