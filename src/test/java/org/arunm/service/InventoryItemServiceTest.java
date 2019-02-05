package org.arunm.service;

import org.arunm.entity.InventoryItem;
import org.arunm.entity.Manufacturer;
import org.arunm.exception.ItemAlreadyExistsException;
import org.arunm.repository.InventoryItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InventoryItemServiceTest {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;


    private InventoryService inventoryService;

    private static boolean setUpIsDone = false;

    private List<InventoryItem> testInventoryItems = new ArrayList<InventoryItem>();

    @Before
    public void setUp() {
        if (!setUpIsDone) {
            inventoryService = new InventoryService(inventoryItemRepository);
            createTestData();

        }
    }

    @Test(expected = ItemAlreadyExistsException.class)
    public void itemAlreadyExists() {
        InventoryItem item = testInventoryItems.get(0);
        inventoryService.addInventoryItem(item);
    }

    @Test
    public void limitandOffsetNotSet() {
        assertThat(inventoryService.getInventoryItems(0, 0),
                hasSize(10));
    }

    @Test
    public void limitandOffsetTest() {
        assertThat(inventoryService.getInventoryItems(10, 7),
                hasSize(3));
    }

    private void createTestData() {
        this.testInventoryItems = IntStream.rangeClosed(1, 10)
                .mapToObj((index) -> createValidInventoryItem(index))
                .collect(Collectors.toList());

        this.inventoryItemRepository.saveAll(this.testInventoryItems);

    }


    private InventoryItem createValidInventoryItem(int index) {
        InventoryItem inventoryItem = new InventoryItem();
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setName("test" + index);
        manufacturer.setHomePage("http://www.google.com");
        manufacturer.setPhone("(07) 5556 4321");

        inventoryItem.setManufacturer(manufacturer);
        inventoryItem.setName("item" + index);
        inventoryItem.setReleaseDate(LocalDateTime.now());

        return inventoryItem;
    }


}
