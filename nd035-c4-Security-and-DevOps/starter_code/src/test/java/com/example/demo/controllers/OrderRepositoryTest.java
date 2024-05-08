package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderRepositoryTest
{
    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void SetUp()
    {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepo);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepo);
    }
    @Test
    public void submitOrder()
    {
        Cart cart = new Cart();
        Item item = new Item();
        User user = new User();

        item.setId(44L);
        item.setName("baseball");
        item.setPrice(new BigDecimal(100));

        user.setId(1L);
        user.setUsername("jim");
        user.setCart(cart);

        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(item.getPrice());

        when(userRepo.findByUsername("jim")).thenReturn(user);
        ResponseEntity<UserOrder> response=orderController.submit("jim");
        assertEquals(200,response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(user,userOrder.getUser());
        assertEquals(item,userOrder.getItems().get(0));
        assertEquals(cart.getTotal(),userOrder.getTotal());
    }
}
