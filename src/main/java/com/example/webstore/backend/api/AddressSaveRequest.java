package com.example.webstore.backend.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressSaveRequest {
    @NotNull
    @NotBlank
    private String addressLine1;

    private String addressLine2;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 32)
    private String city;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String country;


    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
