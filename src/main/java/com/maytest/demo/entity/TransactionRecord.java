package com.maytest.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_record")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "trx_amount", nullable = false)
    private Double trxAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "trx_date")
    private java.sql.Date trxDate;

    @Column(name = "trx_time")
    private String trxTime;

    @Column(name = "customer_id")
    private String customerId;

    @Version
    private Long version;
}