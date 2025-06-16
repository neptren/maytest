package com.maytest.demo.repository;

import com.maytest.demo.entity.TransactionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

    Page<TransactionRecord> findByCustomerIdContainingAndAccountNumberContainingAndDescriptionContaining(
        String customerId, String accountNumber, String description, Pageable pageable
    );
}