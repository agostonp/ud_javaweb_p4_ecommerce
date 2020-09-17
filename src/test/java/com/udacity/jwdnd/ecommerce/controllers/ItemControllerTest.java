package com.udacity.jwdnd.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.udacity.jwdnd.ecommerce.model.persistence.Item;
import com.udacity.jwdnd.ecommerce.model.persistence.repositories.ItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository;
    
    @BeforeEach
    public void initEachTest() {
        itemRepository = initMockItemRepository();
        itemController = new ItemController(itemRepository);
    }

    @Test
    public void getItems() {
        List<Item> expectedItemList = generateTestItemList(3);
        when(itemRepository.findAll()).thenReturn(generateTestItemList(3));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> foundItemList = response.getBody();
        assertNotNull(foundItemList);
        assertEquals(expectedItemList.size(), foundItemList.size());
        assertEquals(expectedItemList.get(2).getName(), foundItemList.get(2).getName());
        assertEquals(expectedItemList.get(2).getPrice(), foundItemList.get(2).getPrice());
        assertEquals(expectedItemList.get(2).getDescription(), foundItemList.get(2).getDescription());
    }

    @Test
    public void getItemById() {
        Item expectedItem = generateTestItem(0);
        ResponseEntity<Item> response = itemController.getItemById(expectedItem.getId());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Item foundItem = response.getBody();
        assertNotNull(foundItem);
        assertEquals(expectedItem.getId(), foundItem.getId());
        assertEquals(expectedItem.getName(), foundItem.getName());
        assertEquals(expectedItem.getPrice(), foundItem.getPrice());
        assertEquals(expectedItem.getDescription(), foundItem.getDescription());
    }

    @Test
    public void getItemsByName() {
        List<Item> expectedItemList = generateTestItemList(2);
        when(itemRepository.findByName(expectedItemList.get(0).getName())).thenReturn(generateTestItemList(2));

        ResponseEntity<List<Item>> response = itemController.getItemsByName(expectedItemList.get(0).getName());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> foundItemList = response.getBody();
        assertNotNull(foundItemList);
        assertEquals(expectedItemList.size(), foundItemList.size());
        assertEquals(expectedItemList.get(0).getName(), foundItemList.get(0).getName());
        assertEquals(expectedItemList.get(0).getPrice(), foundItemList.get(0).getPrice());
        assertEquals(expectedItemList.get(0).getDescription(), foundItemList.get(0).getDescription());
    }


    public static Item generateTestItem(int id) {
        Item item;
        switch(id) {
            case 0:
                item = new Item(Long.valueOf(id), "screwdriver", new BigDecimal("890.00"), "Mighty screwdriver of the ages.");
                break;
            case 1:
                item = new Item(Long.valueOf(id), "screwdriver", new BigDecimal("790.03"), "A second mighty screwdriver of the ages.");
                break;
            default:
                item = new Item(Long.valueOf(id), "hammer", new BigDecimal("1199.99"), "This text describes the hammer.");
        }
        return item;
    }

    public static List<Item> generateTestItemList(int length) {
        List<Item> list = new ArrayList<>();
        for(int i=0; i < length; i++) {
            list.add(generateTestItem(i));
        }
        return list;
    }

    public static ItemRepository initMockItemRepository() {
        ItemRepository itemRepository = mock(ItemRepository.class);
        
        List<Item> itemList = generateTestItemList(3);
        for(int i=0; i < 3; i++) {
            when(itemRepository.findById(itemList.get(i).getId())).thenReturn(Optional.of(generateTestItem(i)));
        }

        return itemRepository;
    }
}
