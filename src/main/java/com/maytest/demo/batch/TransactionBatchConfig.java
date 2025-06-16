package com.maytest.demo.batch;

import com.maytest.demo.entity.TransactionRecord;
import com.maytest.demo.repository.TransactionRecordRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;

import java.sql.Date;

@Configuration
@EnableBatchProcessing
public class TransactionBatchConfig {

    @Autowired
    private TransactionRecordRepository repo;

    @Bean
    public FlatFileItemReader<TransactionRecord> reader() {
        return new FlatFileItemReaderBuilder<TransactionRecord>()
            .name("transactionRecordReader")
            .resource(new ClassPathResource("data.txt"))
            .delimited()
            .delimiter("|")
            .names("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId")
            .linesToSkip(1)
            .fieldSetMapper((FieldSet fieldSet) -> TransactionRecord.builder()
                .accountNumber(fieldSet.readString("accountNumber"))
                .trxAmount(fieldSet.readDouble("trxAmount"))
                .description(fieldSet.readString("description"))
                .trxDate(Date.valueOf(fieldSet.readString("trxDate")))
                .trxTime(fieldSet.readString("trxTime"))
                .customerId(fieldSet.readString("customerId"))
                .build())
            .build();
    }

    @Bean
    public ItemWriter<TransactionRecord> writer() {
        return items -> repo.saveAll(items);
    }

    @Bean
    public Step step(StepBuilderFactory sbf) {
        return sbf.get("step")
            .<TransactionRecord, TransactionRecord>chunk(10)
            .reader(reader())
            .writer(writer())
            .build();
    }

    @Bean
    public Job job(JobBuilderFactory jbf, Step step) {
        return jbf.get("job")
            .incrementer(new RunIdIncrementer())
            .flow(step)
            .end()
            .build();
    }
}