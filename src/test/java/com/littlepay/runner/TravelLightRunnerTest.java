package com.littlepay.runner;

import com.littlepay.service.CSVReaderService;
import com.littlepay.service.CSVWriterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for TravelLightRunner.
 * This class tests the context loading of the application and ensures that the required services are available.
 *
 * @author Sachi
 */
@SpringBootTest
@ActiveProfiles("test") // Use the test profile to avoid loading production configurations
class TravelLightRunnerTest {

    @Autowired
    CSVReaderService csvReaderService;

    @Autowired
    CSVWriterService csvWriterService;

    @Test
    void contextLoads() {
        assertNotNull(csvReaderService);
        assertNotNull(csvWriterService);
    }

    @Test
    void testRun() {
        // @TODO: Implement the test for the run method of TravelLightRunner
    }
}
