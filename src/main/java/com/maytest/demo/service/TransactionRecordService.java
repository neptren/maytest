package com.maytest.demo.service;

import com.maytest.demo.dto.TransactionRecordDTO;
import com.maytest.demo.entity.TransactionRecord;
import com.maytest.demo.repository.TransactionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionRecordService {

    @Autowired
    private TransactionRecordRepository repo;

    public Page<TransactionRecordDTO> search(String customerId, String accountNumber, String description, Pageable pageable) {
        Page<TransactionRecord> page = repo.findByCustomerIdContainingAndAccountNumberContainingAndDescriptionContaining(
            customerId == null ? "" : customerId,
            accountNumber == null ? "" : accountNumber,
            description == null ? "" : description, pageable
        );
        return page.map(this::toDTO);
    }

    public TransactionRecordDTO get(Long id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    @Transactional
    public TransactionRecordDTO updateDescription(Long id, String description, Long version) {
        TransactionRecord record = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        if (!record.getVersion().equals(version))
            throw new RuntimeException("Concurrent modification detected");
        record.setDescription(description);
        repo.save(record);
        return toDTO(record);
    }

    private TransactionRecordDTO toDTO(TransactionRecord e) {
        return TransactionRecordDTO.builder()
            .id(e.getId())
            .accountNumber(e.getAccountNumber())
            .trxAmount(e.getTrxAmount())
            .description(e.getDescription())
            .trxDate(e.getTrxDate() == null ? null : e.getTrxDate().toString())
            .trxTime(e.getTrxTime())
            .customerId(e.getCustomerId())
            .version(e.getVersion())
            .build();
    }
}