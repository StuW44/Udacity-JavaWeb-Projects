package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private CartRepository cartRepo = mock(CartRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    private Cart cart;
    private User user;
    private Item item;

    @Before
    public void SetUp()
    {
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepo);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepo);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);
    }
    @Test
    public void AddToCart()
    {
        ModifyCartRequest cartRequest = setUpCart();

        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
        assertEquals(200,response.getStatusCodeValue());
        Cart cartReturn = response.getBody();
        assertNotNull(cartReturn);
        assertEquals(new BigDecimal(100),cartReturn.getItems().get(0).getPrice());
        assertEquals(new BigDecimal(1000),cartReturn.getTotal());
    }



    @Test
    public void RemoveFromCart()
    {
        ModifyCartRequest cartRequest = setUpCart();
        cart.addItem(item);
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
        assertEquals(200,response.getStatusCodeValue());
        Cart cartReturn = response.getBody();
        assertNotNull(cartReturn);
        assertEquals(user,cartReturn.getUser());
        assertEquals(0,cartReturn.getItems().size());
    }
    @Test
    public void RemoveFromCartFailure_BadUser()
    {
        ModifyCartRequest cartRequest = setUpCart();
        cart.addItem(item);
        cartRequest.setUsername("Bob");
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
        assertEquals(404,response.getStatusCodeValue());

    }
    @Test
    public void RemoveFromCartFailure_BadId()
    {
        ModifyCartRequest cartRequest = setUpCart();
        cart.addItem(item);
        cartRequest.setItemId(9999L);
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
        assertEquals(404,response.getStatusCodeValue());

    }

    private ModifyCartRequest setUpCart() {
        cart = new Cart();
        item = new Item();
        user = new User();

        item.setId(44L);
        item.setName("baseball");
        item.setPrice(new BigDecimal(100));

        user.setId(1L);
        user.setUsername("jim");
        user.setCart(cart);

        cart.setUser(user);

        when(itemRepository.findById(44L)).thenReturn(Optional.of(item));
        when(userRepo.findByUsername("jim")).thenReturn(user);
        ModifyCartRequest cartRequest = new ModifyCartRequest();

        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(10);

        return cartRequest;
    }
}
