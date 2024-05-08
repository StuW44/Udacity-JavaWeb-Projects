package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository= mock(ItemRepository .class);;
    @Before
    public void SetUp()
    {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);
    }
    @Test
    public void GetItemByName()
    {
        Item item = new Item();
        item.setId(100L);
        item.setName("turkey");
        item.setDescription("toy turkey");
        List<Item> items = Collections.singletonList(item);

        when(itemRepository.findByName("turkey")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("turkey");
        assertEquals(200,response.getStatusCodeValue());
        List<Item> listitems = response.getBody();
        assertNotNull(listitems);
        Item first = listitems.get(0);
        assertNotNull(first);
        assertEquals("turkey",first.getName());

    }
    @Test
    public void GetItemByNameFailure()
    {
        Item item = new Item();
        item.setId(100L);
        item.setName("turkey");
        List<Item> items = Collections.singletonList(item);
        when(itemRepository.findByName("turkey")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("chicken");
        assertEquals(404,response.getStatusCodeValue());
    }
    @Test
    public void GetItemById()
    {
        Item item = new Item();
        item.setId(10L);
        item.setPrice(BigDecimal.valueOf(1000.0));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(10L);
        assertEquals(200,response.getStatusCodeValue());
        Item i =  response.getBody();
        assertNotNull(i);
        assertEquals(Optional.of(10L), Optional.ofNullable(i.getId()));
        assertEquals(BigDecimal.valueOf(1000.0), i.getPrice());
    }
    @Test
    public void GetItemByIdFailure()
    {
        Item item = new Item();
        item.setId(10L);
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(9L);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    public void GetAllItems()
    {
        Item item = new Item();
        item.setId(10L);
        item.setName("test");
        item.setPrice(BigDecimal.valueOf(999));
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200,response.getStatusCodeValue());
        List<Item> items =  response.getBody();
        assertNotNull(items);
        assertEquals(1,items.size());
        assertEquals(item,items.get(0));
    }
}
