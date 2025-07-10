package com.littlepay.service;

import com.littlepay.model.InputRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.littlepay.service.TestRecordProcessingFixtures.COMBINED_INPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for CSVReadService class.
 * It additionally tests Spring configs as well.
 *
 * @author Sachi
 */
@SpringBootTest()
@ContextConfiguration(classes = {
        CSVReaderService.class
})
public class CSVReaderServiceTest {

    @Autowired
    private CSVReaderService csvReaderService;

    @Test
    public void testReadCSVFile() throws Exception {
        String fileName = "src/test/resources/test_read_csv_input.csv";
        List<InputRecord> actual= csvReaderService.process(fileName);

        // Validate the records read from the CSV file
        assertEquals(COMBINED_INPUT, actual);
    }
}
