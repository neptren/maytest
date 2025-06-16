package com.maytest.controller;

import com.maytest.dto.TransactionRecordDto;
import com.maytest.entity.TransactionRecord;
import com.maytest.service.TransactionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRecordController {

    @Autowired
    private TransactionRecordService service;

    // Search and paginate
    @GetMapping
    public Page<TransactionRecordDto> search(
        @RequestParam(required = false) String customerId,
        @RequestParam(required = false) String accountNumber,
        @RequestParam(required = false) String description,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return service.search(customerId, accountNumber, description, PageRequest.of(page, size));
    }

    // Update description
    @PutMapping("/{id}")
    public ResponseEntity<TransactionRecordDto> updateDescription(
            @PathVariable Long id,
            @RequestBody TransactionRecordDto dto
    ) {
        TransactionRecordDto updated = service.updateDescription(id, dto.getDescription(), dto.getVersion());
        return ResponseEntity.ok(updated);
    }
}