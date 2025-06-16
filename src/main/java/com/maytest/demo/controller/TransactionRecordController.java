package com.maytest.demo.controller;

import com.maytest.demo.dto.TransactionRecordDTO;
import com.maytest.demo.service.TransactionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRecordController {

    @Autowired
    private TransactionRecordService service;

    // GET with search and pagination
    @GetMapping
    public Page<TransactionRecordDTO> search(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return service.search(customerId, accountNumber, description, pageable);
    }

    // GET by ID
    @GetMapping("/{id}")
    public TransactionRecordDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    // PATCH with optimistic locking
    @PatchMapping("/{id}/description")
    public TransactionRecordDTO updateDescription(
            @PathVariable Long id,
            @RequestParam String description,
            @RequestParam Long version) {
        return service.updateDescription(id, description, version);
    }
}