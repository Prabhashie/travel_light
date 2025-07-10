package com.littlepay.runner;

import com.littlepay.model.InputRecord;
import com.littlepay.model.OutputRecord;
import com.littlepay.service.CSVReaderService;
import com.littlepay.service.CSVWriterService;
import com.littlepay.service.RecordProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
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
public class TravelLightRunner implements CommandLineRunner {

    public static final Logger LOG = LoggerFactory.getLogger(TravelLightRunner.class);

    private final CSVReaderService csvReaderService;

    private final CSVWriterService csvWriterService;

    private final RecordProcessingService recordProcessingService;

    @Value("${input.file.path}")
    private String inputFileName;

    @Value("${output.file.path}")
    private String outputFileName;

    public TravelLightRunner(CSVReaderService csvReaderService, CSVWriterService csvWriterService, RecordProcessingService recordProcessingService) {
        this.csvReaderService = csvReaderService;
        this.csvWriterService = csvWriterService;
        this.recordProcessingService = recordProcessingService;
    }

    @Override
    public void run(String... args) throws IOException {
        LOG.info("Running TravelLightRunner...");

        if (inputFileName == null || inputFileName.isEmpty()) {

            LOG.error("Input file path is not set or is empty. Please set the 'input.file.path' property.");
            throw new IOException("Input file path is not set or is empty. Please set the 'input.file.path' property.");
        }
        if (outputFileName == null || outputFileName.isEmpty()) {
            LOG.error("Output file path is not set or is empty. Please set the 'output.file.path' property.");
            throw new IOException("Output file path is not set or is empty. Please set the 'output.file.path' property.");
        }

        LOG.info("Input file name: {} and output file name: {}", inputFileName, outputFileName);

        List<InputRecord> inputRecords = null;

        // Read the input CSV file
        try {
            inputRecords = csvReaderService.process(inputFileName);
        } catch (IOException e) {
            // Log error and exit without throwing the plain exception for better user experience
            LOG.error("Error reading CSV file: {}", e.getMessage());
            throw new IOException("Error reading CSV file: " + e.getMessage());
        }

        // Return early if no input records are found
        if (inputRecords == null || inputRecords.isEmpty()) {
            LOG.warn("No input records found in the file: {}", inputFileName);
            return;
        }

        // Process the input records to generate output records
        List<OutputRecord> outputRecords = recordProcessingService.processRecords(inputRecords);

        // Return early if no output records are generated
        if (outputRecords == null || outputRecords.isEmpty()) {
            LOG.warn("No output records generated from the input records");
            return;
        }

        // Write the output to a CSV file
        try {
            csvWriterService.process(outputFileName, outputRecords);
        } catch (IOException e) {
            // Log error and exit without throwing the plain exception for better user experience
            LOG.error("Error writing to CSV file: {}", e.getMessage());
            throw new IOException("Error writing to CSV file: " + e.getMessage());
        }
    }
}
