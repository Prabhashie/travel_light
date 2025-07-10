package com.littlepay.service;

import com.littlepay.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to read and process CSV files containing input records.
 * This service reads a CSV file, parses its content, and converts it into a list of InputRecord objects.
 *
 *  @author Sachi
 */
@Service
public class CSVReaderService extends FileReaderService {

    public static final Logger LOG = LoggerFactory.getLogger(CSVReaderService.class);

    private static final String[] HEADERS = { "ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN"};

    @Override
    public List<InputRecord> process(String fileName) throws IOException {
        LOG.info("Reading CSV file {}", fileName);

        // Create a CSVFormat instance with headers and skip the header record
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();

        // Enable try-with-resources to ensure the Reader is closed automatically
        try(
            Reader in = new FileReader(fileName);
        ) {
            List<InputRecord> inputRecords = new ArrayList<>();
            Iterable<CSVRecord> records = csvFormat.parse(in);

            for (CSVRecord record : records) {
                InputRecord inputRecord = convertToInputRecord(record);
                inputRecords.add(inputRecord);
            }

            return inputRecords;
        }
    }

    /**
     * Converts a CSVRecord to an InputRecord.
     *
     * @param record the CSVRecord to convert
     * @return the converted InputRecord
     */
    private InputRecord convertToInputRecord(CSVRecord record) {
        LOG.trace("Converting InputRecord to InputRecord");

        int id = Integer.parseInt(record.get("ID").trim());

        // parse the date time string to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm");
        LocalDateTime dateTimeUTC = LocalDateTime.parse(record.get("DateTimeUTC").trim(), formatter);

        TapType tapType = TapType.valueOf(record.get("TapType").trim());
        Stop stop = new Stop(record.get("StopId").trim());
        Company company =  new Company(record.get("CompanyId").trim());
        Bus bus = new Bus(record.get("BusID").trim());
        Long pan = Long.parseLong(record.get("PAN").trim());

        return InputRecord.builder()
                .id(id)
                .dateTimeUTC(dateTimeUTC)
                .tapType(tapType)
                .stopId(stop)
                .companyId(company)
                .busId(bus)
                .pan(pan)
                .build();

    }
}
