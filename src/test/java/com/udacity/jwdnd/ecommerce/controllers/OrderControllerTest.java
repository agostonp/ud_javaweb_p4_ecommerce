package com.udacity.jwdnd.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.udacity.jwdnd.ecommerce.model.persistence.Cart;
import com.udacity.jwdnd.ecommerce.model.persistence.User;
import com.udacity.jwdnd.ecommerce.model.persistence.UserOrder;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.OrderRepository;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @BeforeEach
    public void initEachTest() {
        userRepository = mock(UserRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderController = new OrderController(userRepository, orderRepository);
    }

    @Test
    public void submit() {
        Cart inputCart = generateTestCart();
        when(userRepository.findByUsername(inputCart.getUser().getUsername())).thenReturn(inputCart.getUser());

        ResponseEntity<UserOrder> response = orderController.submit(inputCart.getUser().getUsername());
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserOrder newOrder = response.getBody();
        assertNotNull(newOrder);
        assertEquals(inputCart.getItems().size(), newOrder.getItems().size());
        assertEquals(inputCart.getItems().get(0).getId(), newOrder.getItems().get(0).getId());
        assertEquals("screwdriver", newOrder.getItems().get(1).getName());
        assertNotNull(newOrder.getUser());
        assertEquals(inputCart.getUser().getUsername(), newOrder.getUser().getUsername());
        BigDecimal total = new BigDecimal("4080.01");
        assertEquals(0, total.compareTo(newOrder.getTotal()));
    }

    

    private Cart generateTestCart() {
        Cart cart = new Cart();
        User user = new User(1L, "spiderman", "best passwORD", cart);
        cart.setId(0L);
        cart.setUser(user);
        for(int i=0; i<4; i++) {
            cart.addItem(ItemControllerTest.generateTestItem(i));
        }
        return cart;
    }

}
