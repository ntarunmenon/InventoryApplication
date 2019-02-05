package org.arunm.repository;


import org.arunm.entity.InventoryItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InventoryItemRepository extends PagingAndSortingRepository<InventoryItem, String> {
}