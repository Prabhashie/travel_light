package com.littlepay;

import com.littlepay.runner.TravelLightRunner;
import com.littlepay.service.CSVReaderService;
import com.littlepay.service.CSVWriterService;
import com.littlepay.service.RecordProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end test for the TravelLight application.
 *
 * @author Sachi
 */
@SpringBootTest
@ActiveProfiles("test") // Use the test profile to avoid loading production configurations
@TestPropertySource("classpath:application-test.properties")
public class TravelLightE2ETest {

    private static final Logger LOG = LoggerFactory.getLogger(TravelLightE2ETest.class);

    @Autowired
    private CSVReaderService csvReaderService;

    @Autowired
    private CSVWriterService csvWriterService;

    @Autowired
    private RecordProcessingService recordProcessingService;

    @Autowired
    private TravelLightRunner travelLightRunner;

    @Value("${output.file.path}")
    private String outputFileName;

    @BeforeEach
    void cleanUpOutputFile() throws Exception {
        Path path = Paths.get(outputFileName);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    void testContextLoadsAndRun() {
        LOG.info("Starting end-to-end test for TravelLight application...");

        // verify no output file exists before running the application
        assertFalse(Files.exists(Paths.get(outputFileName)));

        travelLightRunner.run();

        assertNotNull(csvReaderService);
        assertNotNull(csvWriterService);
        assertNotNull(recordProcessingService);

        // test output file creation
        assertTrue(Files.exists(Paths.get(outputFileName)));
    }
}
