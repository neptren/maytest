package com.maytest.demo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionRecordDTO {
    private Long id;
    private String accountNumber;
    private Double trxAmount;
    private String description;
    private String trxDate;
    private String trxTime;
    private String customerId;
    private Long version;
}