package com.littlepay.runner;

import com.littlepay.service.CSVReaderService;
import com.littlepay.service.CSVWriterService;
import com.littlepay.service.RecordProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for TravelLightRunner class.
 * This is a pure unit test class that does not test Spring configs.
 *
 * @author Sachi
 */

public class TravelLightRunnerTest {

    private TravelLightRunner travelLightRunner;

    @BeforeEach
    public void setUp() {
        CSVReaderService csvReaderService;
        CSVWriterService csvWriterService;
        RecordProcessingService recordProcessingService;

        // Mock the services
        csvReaderService = mock(CSVReaderService.class);
        csvWriterService = mock(CSVWriterService.class);
        recordProcessingService = mock(RecordProcessingService.class);

        // Initialize TravelLightRunner with mocked services
        travelLightRunner = new TravelLightRunner(csvReaderService, csvWriterService, recordProcessingService);
    }

    @Test
    public void testHandlingInvalidInputFile() {
        setPrivateField(travelLightRunner, "inputFileName", null);
        setPrivateField(travelLightRunner, "outputFileName", "output.csv");

        assertThrows(IOException.class, () -> travelLightRunner.run());
    }

    @Test
    public void testHandlingInvalidOutputFile() {
        setPrivateField(travelLightRunner, "inputFileName", "input.csv");
        setPrivateField(travelLightRunner, "outputFileName", null);

        assertThrows(IOException.class, () -> travelLightRunner.run());
    }

    /**
     * This method is used to set private fields in the TravelLightRunner class for testing purposes.
     * It uses reflection to access and modify private fields.
     *
     * @param target    The instance of TravelLightRunner
     * @param fieldName The name of the private field to set
     * @param value     The value to set for the private field
     */
    private void setPrivateField(Object target, String fieldName, String value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
