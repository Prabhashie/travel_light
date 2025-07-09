package com.littlepay.runner;

import com.littlepay.model.InputRecord;
import com.littlepay.service.CSVReaderService;
import com.littlepay.service.CSVWriterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * CommandLineRunner implementation to read input CSV, process the records, and write output CSV.
 * This class is executed at application startup.
 *
 * @author Sachi
 */
@Component
@Profile("!test") // Exclude this runner from the test profile
public class TravelLightRunner implements CommandLineRunner {

    private final CSVReaderService csvReaderService;

    private final CSVWriterService csvWriterService;

    @Value("${input.file.path}")
    private String inputFileName;

    @Value("${output.file.path}")
    private String outputFileName;

    public TravelLightRunner(CSVReaderService csvReaderService, CSVWriterService csvWriterService) {
        this.csvReaderService = csvReaderService;
        this.csvWriterService = csvWriterService;
    }

    @Override
    public void run(String... args) {
        // Read the input CSV file
        try {
            List<InputRecord> inputRecords = csvReaderService.process(inputFileName);
        } catch (IOException e) {
            // Log error and exit without throwing the plain exception for better user experience
            System.err.println("Error reading CSV file: " + e.getMessage());
            System.exit(1);
        }

        // Process the input records

        // Write the output to a CSV file

    }
}
