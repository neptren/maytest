package com.maytest.batch;

import com.maytest.entity.TransactionRecord;
import org.springframework.batch.item.ItemProcessor;

public class TransactionItemProcessor implements ItemProcessor<TransactionRecord, TransactionRecord> {
    @Override
    public TransactionRecord process(TransactionRecord item) throws Exception {
        // Additional processing/validation if needed
        return item;
    }
}