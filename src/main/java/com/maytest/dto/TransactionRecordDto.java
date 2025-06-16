package com.maytest.dto;

import lombok.Data;

@Data
public class TransactionRecordDto {
    private Long id;
    private String accountNumber;
    private Double trxAmount;
    private String description;
    private String trxDate;
    private String trxTime;
    private String customerId;
    private Long version;
}