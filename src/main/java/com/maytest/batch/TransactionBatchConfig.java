package com.maytest.batch;

import com.maytest.entity.TransactionRecord;
import com.maytest.repository.TransactionRecordRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class TransactionBatchConfig {

    @Bean
    public FlatFileItemReader<TransactionRecord> reader(
            @Value("${input.file}") String resource) {
        return new FlatFileItemReaderBuilder<TransactionRecord>()
            .name("transactionItemReader")
            .resource(new FileSystemResource(resource))
            .linesToSkip(1)
            .delimited()
            .delimiter("|")
            .names("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId")
            .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(TransactionRecord.class);
            }})
            .build();
    }

    @Bean
    public TransactionItemProcessor processor() {
        return new TransactionItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<TransactionRecord> writer(TransactionRecordRepository repository) {
        RepositoryItemWriter<TransactionRecord> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job importTransactionJob(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("importTransactionJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,
                      ItemReader<TransactionRecord> reader,
                      ItemProcessor<TransactionRecord, TransactionRecord> processor,
                      ItemWriter<TransactionRecord> writer) {
        return stepBuilderFactory.get("step1")
                .<TransactionRecord, TransactionRecord> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}