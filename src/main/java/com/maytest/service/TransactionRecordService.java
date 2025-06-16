package com.maytest.service;

import com.maytest.dto.TransactionRecordDto;
import com.maytest.entity.TransactionRecord;
import com.maytest.repository.TransactionRecordRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionRecordService {
    @Autowired
    private TransactionRecordRepository repo;

    public Page<TransactionRecordDto> search(String customerId, String accountNumber, String description, Pageable pageable) {
        Specification<TransactionRecord> spec = Specification.where(null);

        if (customerId != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("customerId"), customerId));
        if (accountNumber != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("accountNumber"), accountNumber));
        if (description != null)
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));

        Page<TransactionRecord> page = repo.findAll(spec, pageable);

        return page.map(this::toDto);
    }

    public TransactionRecordDto updateDescription(Long id, String newDescription, Long version) {
        TransactionRecord record = repo.findById(id).orElseThrow();
        if (!record.getVersion().equals(version)) {
            throw new OptimisticLockException("Record was updated by another transaction!");
        }
        record.setDescription(newDescription);
        repo.save(record);
        return toDto(record);
    }

    private TransactionRecordDto toDto(TransactionRecord entity) {
        TransactionRecordDto dto = new TransactionRecordDto();
        dto.setId(entity.getId());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setTrxAmount(entity.getTrxAmount());
        dto.setDescription(entity.getDescription());
        dto.setTrxDate(entity.getTrxDate());
        dto.setTrxTime(entity.getTrxTime());
        dto.setCustomerId(entity.getCustomerId());
        dto.setVersion(entity.getVersion());
        return dto;
    }
}