package com.littlepay;

import com.littlepay.model.InputRecord;
import com.littlepay.service.CSVReaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * End-to-end test for the Travel Light application.
 *
 * @author Sachi
 */
@SpringBootTest
@ActiveProfiles("test") // Use the test profile to avoid loading production configurations
class TravelLightE2ETest {

    @Autowired
    CSVReaderService csvReaderService;

    @Test
    void csvReadProcessTest() throws IOException {
        List<InputRecord> inputRecords = csvReaderService.process("src/test/resources/input.csv");

        assertFalse(inputRecords.isEmpty());
    }
}
