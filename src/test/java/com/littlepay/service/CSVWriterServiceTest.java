package com.littlepay.service;

import com.littlepay.model.InputRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.littlepay.service.TestRecordProcessingFixtures.COMBINED_OUTPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for CSVReadService class.
 * It additionally tests Spring configs as well.
 *
 * @author Sachi
 */
@SpringBootTest()
@ContextConfiguration(classes = {
        CSVWriterService.class
})
public class CSVWriterServiceTest {

    @Autowired
    private CSVWriterService csvWriterService;

    private final String ACTUAL_FILE = "src/test/resources/test_write_csv_actual_output.csv";
    private final String EXPECTED_FILE = "src/test/resources/test_write_csv_expected_output.csv";

    private final Path ACTUAL = Paths.get(ACTUAL_FILE);
    private final Path EXPECTED = Paths.get(EXPECTED_FILE);

    @BeforeEach
    void setUp() throws Exception {
        if (Files.exists(ACTUAL)) {
            Files.delete(ACTUAL);
        }
    }

    @Test
    public void testReadCSVFile() throws Exception {

        csvWriterService.process(ACTUAL_FILE, COMBINED_OUTPUT);

        List<String> lines1 = Files.readAllLines(ACTUAL);
        List<String> lines2 = Files.readAllLines(EXPECTED);

        assertEquals(lines1.size(), lines2.size(), "CSV files have different number of lines");

        for (int i = 0; i < lines1.size(); i++) {
            assertEquals(lines1.get(i), lines2.get(i), "Line " + (i + 1) + " does not match");
        }
    }
}
