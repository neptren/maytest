package com.maytest.demo;

import com.maytest.demo.entity.TransactionRecord;
import com.maytest.demo.repository.TransactionRecordRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRecordRepository repo;

    private TransactionRecord record;

    @BeforeEach
    public void setup() {
        repo.deleteAll();
        record = repo.save(TransactionRecord.builder()
            .accountNumber("123")
            .trxAmount(100.0)
            .description("Test Desc")
            .trxDate(java.sql.Date.valueOf("2020-01-01"))
            .trxTime("11:11:11")
            .customerId("CUST1")
            .build());
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/api/transactions/" + record.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountNumber").value("123"));
    }

    @Test
    public void testSearch() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .param("customerId", "CUST1")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].accountNumber").value("123"));
    }

    @Test
    public void testUpdateDescription() throws Exception {
        mockMvc.perform(patch("/api/transactions/" + record.getId() + "/description")
                .param("description", "New Desc")
                .param("version", record.getVersion().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("New Desc"));
    }
}