package com.example.webstore.backend.model.dto;

import com.example.webstore.backend.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {


}
