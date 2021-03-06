package com.udacity.jwdnd.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.udacity.jwdnd.ecommerce.model.persistence.Cart;
import com.udacity.jwdnd.ecommerce.model.persistence.User;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.ItemRepository;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.jwdnd.ecommerce.model.requests.ModifyCartRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository;
	private CartRepository cartRepository;
    private ItemRepository itemRepository;

    @BeforeEach
    public void initEachTest() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        itemRepository = ItemControllerTest.initMockItemRepository();
        cartController = new CartController(userRepository, cartRepository, itemRepository);
    }

    @Test
    public void addTocart() {
        ModifyCartRequest inputCartRequest = generateModifyCartRequest();
        when(userRepository.findByUsername(inputCartRequest.getUsername())).thenReturn(generateTestUser());


        ResponseEntity<Cart> response = cartController.addTocart(inputCartRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart updatedCart = response.getBody();
        assertNotNull(updatedCart);
        assertEquals(inputCartRequest.getQuantity(), updatedCart.getItems().size());
        assertEquals(inputCartRequest.getItemId(), updatedCart.getItems().get(0).getId());
        assertEquals("screwdriver", updatedCart.getItems().get(1).getName());
        assertNotNull(updatedCart.getUser());
        assertEquals(inputCartRequest.getUsername(), updatedCart.getUser().getUsername());
        BigDecimal total = new BigDecimal("1580.06");
        assertEquals(0, total.compareTo(updatedCart.getTotal()));
    }

    @Test
    public void removeFromcart() {
        ModifyCartRequest inputCartRequest = generateModifyCartRequest();
        when(userRepository.findByUsername(inputCartRequest.getUsername())).thenReturn(generateTestUser());


        ResponseEntity<Cart> addResponse = cartController.addTocart(inputCartRequest);
        assertNotNull(addResponse);
        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        inputCartRequest.setQuantity(1);
        ResponseEntity<Cart> removeResponse = cartController.removeFromcart(inputCartRequest);

        assertNotNull(removeResponse);
        assertEquals(HttpStatus.OK, removeResponse.getStatusCode());
        Cart updatedCart = removeResponse.getBody();
        assertNotNull(updatedCart);
        assertEquals(17L, updatedCart.getId());
        assertEquals(inputCartRequest.getQuantity(), updatedCart.getItems().size());
        assertEquals(inputCartRequest.getItemId(), updatedCart.getItems().get(0).getId());
        assertEquals("screwdriver", updatedCart.getItems().get(0).getName());
        assertNotNull(updatedCart.getUser());
        assertEquals(inputCartRequest.getUsername(), updatedCart.getUser().getUsername());
        BigDecimal total = new BigDecimal("790.03");
        assertEquals(0, total.compareTo(updatedCart.getTotal()));
    }

    private User generateTestUser() {
        Cart cart = new Cart();
        User user = new User(1L, "spiderman", "best passwORD", cart);
        cart.setId(17L);
        cart.setUser(user);
        cart.setTotal(null);
        cart.setItems(null);
        return user;
    }

    private ModifyCartRequest generateModifyCartRequest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("spiderman");
        cartRequest.setItemId(1);
        cartRequest.setQuantity(2);
        return cartRequest;
    }

}
