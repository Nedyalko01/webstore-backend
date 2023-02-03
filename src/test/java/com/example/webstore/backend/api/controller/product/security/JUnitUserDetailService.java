package com.example.webstore.backend.api.controller.product.security;

import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class JUnitUserDetailService implements UserDetailsService {

    @Autowired
    private LocalUserDAO localUserDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<LocalUser> optionalUser = localUserDAO.findByUsernameIgnoreCase(username);

        if (optionalUser.isPresent()) {
           return optionalUser.get();
        }
        return null;
    }
}
