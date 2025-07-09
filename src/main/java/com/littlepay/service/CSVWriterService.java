package com.littlepay.service;

import com.littlepay.model.OutputRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Service to write to CSV files containing output records.
 * This service writes to a CSV file using a list of OutputRecord objects.
 *
 * @author Sachi
 */
@Service
public class CSVWriterService extends FileWriterService {

    public static final Logger LOG = LoggerFactory.getLogger(CSVWriterService.class);

    private static final String[] HEADERS = {"Started", "Finished", "DurationSecs", "FromStopId", "ToStopId", "ChargeAmount",
            "CompanyId", "BusID", "PAN", "Status"};

    @Override
    public void process(String fileName, List<OutputRecord> outputRecords) throws IOException {

        LOG.info("Writing to CSV file");

        if (outputRecords == null || outputRecords.isEmpty()) {
            LOG.warn("No records to write to CSV file: {}", fileName);
            return;
        }

        // Write processed records to a CSV file
        StringWriter sw = new StringWriter();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .build();

        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
            for (OutputRecord record : outputRecords) {
                printer.printRecord(
                        record.getStartUTC(),
                        record.getFinishUTC(),
                        record.getDurationSecs(),
                        record.getFromStopId().getStopId(),
                        record.getToStopId().getStopId(),
                        record.getChargeAmount(),
                        record.getCompanyId().getCompanyId(),
                        record.getBusId().getBusId(),
                        record.getPan(),
                        record.getTripStatus().name());
            }
        }
    }
}
