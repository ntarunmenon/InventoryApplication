package org.arunm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.arunm.entity.InventoryItem;
import org.arunm.entity.Manufacturer;
import org.arunm.exception.ItemAlreadyExistsException;
import org.arunm.exception.NotFoundException;
import org.arunm.service.InventoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(InventoryItemController.class)
public class InventoryItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Test
    public void testnoInventoryItemFound() throws Exception {
        given(inventoryService.getInventoryItemById("1"))
                .willThrow(NotFoundException.class);
        this.mockMvc.perform(get("/inventory/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testargumentMismatchException() throws Exception {
        this.mockMvc.perform(get("/inventory?skip=AAA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("bad input parameter"));
    }

    @Test
    public void testItemCreated() throws Exception {
        this.mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(createValidInventoryItem())))
                .andExpect(status().isCreated());
    }


    @Test
    public void testItemCreated_Fails() throws Exception {
        this.mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(createInValidInventoryItem())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid input, object invalid"));
    }

    @Test
    public void testItemCreated_ItemAlreadyExists() throws Exception {
        willThrow(ItemAlreadyExistsException.class).given(inventoryService)
                .addInventoryItem(any(InventoryItem.class));

        this.mockMvc.perform(post("/inventory")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(createValidInventoryItem())))
                .andExpect(status().isConflict())
                .andExpect(content().string("an existing item already exists"));
    }

    private InventoryItem createInValidInventoryItem() {
        InventoryItem inventoryItem = new InventoryItem();
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setName("test");
        manufacturer.setHomePage("http://www.google.com");
        manufacturer.setPhone("InvalidPhoneNumber");

        inventoryItem.setManufacturer(manufacturer);
        inventoryItem.setName("item");
        inventoryItem.setReleaseDate(LocalDateTime.now());

        return inventoryItem;
    }

    private InventoryItem createValidInventoryItem() {
        InventoryItem inventoryItem = new InventoryItem();
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setName("test");
        manufacturer.setHomePage("http://www.google.com");
        manufacturer.setPhone("(07) 5556 4321");

        inventoryItem.setManufacturer(manufacturer);
        inventoryItem.setName("item");
        inventoryItem.setReleaseDate(LocalDateTime.now());

        return inventoryItem;
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.CLOSE_CLOSEABLE.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new StdDateFormat());
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
