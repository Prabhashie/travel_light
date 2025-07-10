package com.littlepay.service;

import com.littlepay.model.OutputRecord;
import com.littlepay.repository.RouteDataRepository;
import com.littlepay.repository.StopDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.littlepay.service.TestRecordProcessingFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest()
@ContextConfiguration(classes = {
        RecordProcessingService.class,
        RouteDataRepository.class,
        StopDataRepository.class
})
public class RecordProcessingServiceTest {

    @Autowired
    private RecordProcessingService recordProcessingService;

    @Test
    public void testProcessRecordsCompleteOnly() {
        List<OutputRecord> actual = recordProcessingService.processRecords(ALL_COMPLETE_INPUT);
        assertEquals(ALL_COMPLETE_OUTPUT, actual);
    }

    @Test
    public void testProcessRecordsIncompleteOnly() {
        List<OutputRecord> actual = recordProcessingService.processRecords(ALL_INCOMPLETE_INPUT);
        assertEquals(ALL_INCOMPLETE_OUTPUT, actual);
    }

    @Test
    public void testProcessRecordsCancelledOnly() {
        List<OutputRecord> actual = recordProcessingService.processRecords(ALL_CANCELLED_INPUT);
        assertEquals(ALL_CANCELLED_OUTPUT, actual);
    }

    @Test
    public void testProcessRecordsAllStatuses() {
        List<OutputRecord> actual = recordProcessingService.processRecords(COMBINED_INPUT);
        assertEquals(COMBINED_OUTPUT, actual);
    }

}
