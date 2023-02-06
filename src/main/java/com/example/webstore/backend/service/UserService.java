package com.example.webstore.backend.service;

import com.example.webstore.backend.api.model.LoginBody;
import com.example.webstore.backend.api.model.PasswordResetRequest;
import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.EmailFailureException;
import com.example.webstore.backend.exception.EmailNotFoundException;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.exception.UserNotVerifiedException;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.VerificationToken;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import com.example.webstore.backend.model.dto.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final LocalUserDAO localUserDAO;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final VerificationTokenDAO verificationTokenDAO;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService, EmailService emailService, VerificationTokenDAO verificationTokenDAO) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenDAO = verificationTokenDAO;
    }

    public LocalUser registerUser(RegistrationRequest registration) throws UserAlreadyExistsException, EmailFailureException {

        if (localUserDAO.findByEmailIgnoreCase(registration.getEmail()).isPresent()
                || localUserDAO.findByUsernameIgnoreCase(registration.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setPassword(encryptionService.encryptPassword(registration.getPassword()));

        VerificationToken verificationToken = createVerificationToken(user);

        emailService.sendVerificationEmail(verificationToken);

        user = localUserDAO.save(user);
        return user;
    }


    private VerificationToken createVerificationToken(LocalUser user) {

        VerificationToken verificationToken = new VerificationToken();

        verificationToken.setToken(jwtService.generateJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }


    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {

        Optional<LocalUser> optionalUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());

        if (optionalUser.isPresent()) {
            LocalUser user = optionalUser.get();

            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.isEmailVerified()) {
                    return jwtService.generateJWT(user);

                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();

                    boolean resend = verificationTokens.size() == 0 ||
                            verificationTokens.get(0).getCreatedTimestamp()
                                    .before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));

                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }

                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> optionalToken = verificationTokenDAO.findByToken(token);

        if (optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();
            LocalUser user = verificationToken.getUser();

            if (!user.isEmailVerified()) {
                user.setEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public List<LocalUser> getAllUsers() {
        return localUserDAO.findAll();

    }

    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {

        Optional<LocalUser> optionalUser = localUserDAO.findByEmailIgnoreCase(email);

        if (optionalUser.isPresent()) {
            LocalUser user = optionalUser.get();
            String token = jwtService.generatePasswordResetJWT(user);
            emailService.sendPasswordResetEmail(user, token);
        } else {
            throw new EmailNotFoundException();
        }
    }

    public void resetPassword(PasswordResetRequest request) {

        String email = jwtService.getResetPasswordEmail(request.getToken());

        Optional<LocalUser> optionalUser = localUserDAO.findByEmailIgnoreCase(email);

        if (optionalUser.isPresent()) {
            LocalUser user = optionalUser.get();
            user.setPassword(encryptionService.encryptPassword(request.getPassword()));
            localUserDAO.save(user);
        }
    }
}