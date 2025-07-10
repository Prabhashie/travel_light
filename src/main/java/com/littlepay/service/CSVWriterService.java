package com.littlepay.service;

import com.littlepay.model.OutputRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
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

        LOG.info("Writing to CSV file: {}", fileName);

        if (outputRecords == null || outputRecords.isEmpty()) {
            LOG.warn("No records to write to CSV file: {}", fileName);
            return;
        }

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .build();

        try (
            Writer writer = new FileWriter(fileName);
        ) {
            CSVPrinter printer = new CSVPrinter(writer, csvFormat);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm");

            for (OutputRecord record : outputRecords) {
                printer.printRecord(
                        record.getStartUTC() != null ? record.getStartUTC().format(formatter) : "N/A",
                        record.getFinishUTC() != null ? record.getFinishUTC().format(formatter) : "N/A",
                        record.getDurationSecs() != null ? record.getDurationSecs() : "N/A",
                        record.getFromStopId().getStopId(),
                        record.getToStopId() != null? record.getToStopId().getStopId() : "N/A",
                        record.getChargeAmount(),
                        record.getCompanyId().getCompanyId(),
                        record.getBusId().getBusId(),
                        record.getPan(),
                        record.getTripStatus().name());
            }
        }
    }
}
