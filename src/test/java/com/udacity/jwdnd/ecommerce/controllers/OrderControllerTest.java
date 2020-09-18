package com.udacity.jwdnd.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void getOrdersForUser() {
        List<UserOrder> expectedOrderList = generateTestOrderList();
        UserOrder expectedOrder = expectedOrderList.get(0);

        when(userRepository.findByUsername(expectedOrderList.get(0).getUser().getUsername()))
                .thenReturn(expectedOrderList.get(0).getUser());
        when(orderRepository.findByUser(any(User.class))).thenReturn(generateTestOrderList());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(expectedOrderList.get(0).getUser().getUsername());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserOrder> newOrderList = response.getBody();
        assertNotNull(newOrderList);
        assertEquals(expectedOrderList.size(), newOrderList.size());

        UserOrder newOrder = newOrderList.get(0);
        assertNotNull(newOrder);
        assertEquals(29L, newOrder.getId());
        assertEquals(expectedOrder.getItems().size(), newOrder.getItems().size());
        assertEquals(expectedOrder.getItems().get(0).getId(), newOrder.getItems().get(0).getId());
        assertEquals("screwdriver", newOrder.getItems().get(1).getName());
        assertNotNull(newOrder.getUser());
        assertEquals(expectedOrder.getUser().getUsername(), newOrder.getUser().getUsername());
        BigDecimal total = new BigDecimal("4080.01");
        assertEquals(0, total.compareTo(newOrder.getTotal()));
    }

    private List<UserOrder> generateTestOrderList() {
        Cart cart = generateTestCart();
        UserOrder order = UserOrder.createFromCart(cart);
        order.setId(29L);

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(order);
        return orderList;
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
