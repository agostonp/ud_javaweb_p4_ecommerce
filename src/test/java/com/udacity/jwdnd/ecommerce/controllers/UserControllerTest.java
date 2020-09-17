package com.udacity.jwdnd.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.udacity.jwdnd.ecommerce.model.persistence.User;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.jwdnd.ecommerce.model.requests.CreateUserRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @BeforeEach
    public void initEachTest() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        userController = new UserController(userRepository, cartRepository, bCryptPasswordEncoder);
    }

    @Test
    public void findById() {
        User expectedUser = generateTestUser();
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.of(generateTestUser()));
        ResponseEntity<User> response = userController.findById(expectedUser.getId());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User foundUser = response.getBody();
        assertNotNull(foundUser);
        assertEquals(expectedUser.getId(), foundUser.getId());
        assertEquals(expectedUser.getUsername(), foundUser.getUsername());
    }

    @Test
    public void findByUserName() {
        User expectedUser = generateTestUser();
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(generateTestUser());

        ResponseEntity<User> response = userController.findByUserName(expectedUser.getUsername());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User foundUser = response.getBody();
        assertNotNull(foundUser);
        assertEquals(expectedUser.getId(), foundUser.getId());
        assertEquals(expectedUser.getUsername(), foundUser.getUsername());
    }

    @Test
    public void createUser() {
        CreateUserRequest userRequest = generateCreateUserRequest();
        String encodedPassword = "EncodedPassword";
        when(bCryptPasswordEncoder.encode(userRequest.getPassword())).thenReturn(encodedPassword);

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User newUser = response.getBody();
        assertNotNull(newUser);
        assertEquals(userRequest.getUsername(), newUser.getUsername());
        assertEquals(encodedPassword, newUser.getPassword());
    }

    @Test
    public void createUserInvalidPassword() {
        CreateUserRequest invalidRequest = new CreateUserRequest("Beni", "Short", "Short");

        ResponseEntity<User> response = userController.createUser(invalidRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        User nullUser = response.getBody();
        assertNull(nullUser);
    }

    private User generateTestUser() {
        User user = new User(1L, "Beni", "EncodedPassword", null);
        return user;
    }

    private CreateUserRequest generateCreateUserRequest() {
        CreateUserRequest userRequest = new CreateUserRequest("Beni", "PlainPassword", "PlainPassword");
        return userRequest;
    }
}
