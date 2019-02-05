package org.arunm.service;

import org.arunm.entity.InventoryItem;
import org.arunm.exception.ItemAlreadyExistsException;
import org.arunm.exception.NotFoundException;
import org.arunm.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private InventoryItemRepository inventoryItemRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItem> getInventoryItems(int limit, int offset) {
        return inventoryItemRepository.findAll(new PageRequestWithOffset(limit,offset)).stream().collect(Collectors.toList());

    }

    public void addInventoryItem(InventoryItem inventoryItem){
        if(this.inventoryItemRepository.existsById(inventoryItem.getId())){
            throw new ItemAlreadyExistsException();
        }
        this.inventoryItemRepository.save(inventoryItem);
    }

    public InventoryItem getInventoryItemById(String id){

        return this.inventoryItemRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}