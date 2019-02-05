package org.arunm.controller;

import org.arunm.entity.InventoryItem;
import org.arunm.exception.ItemAlreadyExistsException;
import org.arunm.exception.NotFoundException;
import org.arunm.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@Validated
public class InventoryItemController {

    private InventoryService inventoryService;

    public InventoryItemController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping()
    public List<InventoryItem> getInventoryItems(@RequestParam(value = "limit", defaultValue = "0") int limit, @RequestParam(value = "skip", defaultValue = "0") int offset) {
        return inventoryService.getInventoryItems(limit, offset);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getToDoById(@PathVariable String id) {
        return ResponseEntity.ok(inventoryService.getInventoryItemById(id));
    }

    @PostMapping
    public ResponseEntity addInventoryItem(@Valid @RequestBody InventoryItem inventoryItem) {
        inventoryService.addInventoryItem(inventoryItem);
        return ResponseEntity.status(HttpStatus.CREATED).body("item created");
    }
}

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String noInventoryItemFound() {
        return "not found";
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public String itemAlreadyExists() {
        return "an existing item already exists";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> argumentMismatchException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad input parameter");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid input, object invalid");
    }
}