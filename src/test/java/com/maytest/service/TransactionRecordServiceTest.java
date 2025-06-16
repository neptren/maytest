package com.maytest.service;

import com.maytest.dto.TransactionRecordDto;
import com.maytest.entity.TransactionRecord;
import com.maytest.repository.TransactionRecordRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionRecordServiceTest {

    @Mock
    TransactionRecordRepository repo;

    @InjectMocks
    TransactionRecordService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateDescription_Success() {
        TransactionRecord record = new TransactionRecord(1L, "123", 100.0, "old", "2019-09-11", "11:11:11", "222", 0L);
        when(repo.findById(1L)).thenReturn(Optional.of(record));
        when(repo.save(any())).thenReturn(record);

        TransactionRecordDto dto = service.updateDescription(1L, "new", 0L);
        assertEquals("new", dto.getDescription());
    }

    @Test
    void testUpdateDescription_OptimisticLockException() {
        TransactionRecord record = new TransactionRecord(1L, "123", 100.0, "old", "2019-09-11", "11:11:11", "222", 1L);
        when(repo.findById(1L)).thenReturn(Optional.of(record));
        assertThrows(OptimisticLockException.class, () -> service.updateDescription(1L, "new", 0L));
    }
}