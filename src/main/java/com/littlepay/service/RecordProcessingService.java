package com.littlepay.service;

import com.littlepay.model.InputRecord;
import com.littlepay.model.OutputRecord;
import com.littlepay.model.TapType;
import com.littlepay.model.TripStatus;
import com.littlepay.repository.RouteDataRepository;
import com.littlepay.repository.StopDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for processing input records and generating output records.
 *
 * @author Sachi
 */
@Service
public class RecordProcessingService {

    public static final Logger LOG = LoggerFactory.getLogger(RecordProcessingService.class);

    private final RouteDataRepository routeDataRepository;

    private final StopDataRepository stopDataRepository;

    public RecordProcessingService(RouteDataRepository routeDataRepository, StopDataRepository stopDataRepository) {
        this.routeDataRepository = routeDataRepository;
        this.stopDataRepository = stopDataRepository;
    }

    /**
     * Processes a list of input records and generates output records.
     *
     * @param inputRecords List of input records to process
     * @return List of processed output records
     */
    public List<OutputRecord> processRecords(List<InputRecord> inputRecords) {
        LOG.info("Processing {} input records", inputRecords.size());

        if (inputRecords.isEmpty()) {
            LOG.warn("No input records to process");
            return null;
        }

        // Sort and group input records
        Map<Long, List<InputRecord>> groupedRecords = inputRecords.stream()
                .sorted((Comparator.comparing(InputRecord::getDateTimeUTC))) // Sort by dateTimeUTC
                .collect(Collectors.groupingBy(InputRecord::getPan)); // Group by PAN

        // Generate output records
        List<OutputRecord> outputRecords = generateOutputRecords(groupedRecords);

        LOG.info("Processed {} output records", outputRecords.size());
        return outputRecords;
    }

    /**
     * Generates output records from grouped input records.
     *
     * @param groupedRecords Map of PAN to a list of input records
     * @return List of output records
     */
    private List<OutputRecord> generateOutputRecords(Map<Long, List<InputRecord>> groupedRecords) {
        LOG.info("Generating output records from grouped input records");

        List<OutputRecord> outputRecords = new ArrayList<>();

        for (Map.Entry<Long, List<InputRecord>> entry : groupedRecords.entrySet()) {
            Long pan = entry.getKey();
            List<InputRecord> records = entry.getValue();

            processRecordGroups(pan, records, outputRecords);
        }
        return outputRecords.stream()
                .sorted(Comparator.comparing(OutputRecord::getPan))
                .collect(Collectors.toList()); // Sort output records by PAN
    }

    /**
     * Processes groups of input records for a specific PAN and generates output records.
     *
     * @param pan           PAN associated with the input records
     * @param records       List of input records for the PAN
     * @param outputRecords List to store the generated output records
     */
    private void processRecordGroups(Long pan, List<InputRecord> records, List<OutputRecord> outputRecords) {
        int previousIndex = 0;

        while (previousIndex < records.size()) {
            int currentIndex = previousIndex + 1;
            InputRecord previous = records.get(previousIndex);

            if (currentIndex < records.size()) {
                InputRecord current = records.get(currentIndex);
                if (isTappedOn(previous)) {
                    if (isTappedOff(current) && isSameBus(previous, current) && isSameDate(previous, current)) {
                        TripStatus status = isSameStop(previous, current) ? TripStatus.CANCELLED : TripStatus.COMPLETE;
                        outputRecords.add(buildOutputRecord(previous, current, status));
                        previousIndex += 2;
                        continue;
                    } else {
                        outputRecords.add(buildOutputRecord(previous, null, TripStatus.INCOMPLETE));
                    }
                } else {
                    outputRecords.add(buildOutputRecord(previous, null, TripStatus.INCOMPLETE));
                }
            } else {
                outputRecords.add(buildOutputRecord(previous, null, TripStatus.INCOMPLETE));
            }

            previousIndex++;
        }
    }

    /**
     * Checks if the previous input record indicates a tap ON.
     *
     * @param previous Previous input record
     * @return true if the previous record is a tap ON, false otherwise
     */
    private boolean isTappedOn(InputRecord previous) {
        return previous.getTapType().equals(TapType.ON);
    }

    /**
     * Checks if the current input record indicates a tap OFF.
     *
     * @param current Current input record
     * @return true if the current record is a tap OFF, false otherwise
     */
    private boolean isTappedOff(InputRecord current) {
        return current.getTapType().equals(TapType.OFF);
    }

    /**
     * Checks if the current input record is for the same stop as the previous input record.
     *
     * @param previous Previous input record
     * @param current Current input record
     * @return true if both records are for the same stop, false otherwise
     */
    private boolean isSameStop(InputRecord previous, InputRecord current) {
        return current.getStopId().equals(previous.getStopId());
    }

    /**
     * Checks if the current input record is for the same bus as the previous input record.
     *
     * @param previous Previous input record
     * @param current Current input record
     * @return true if both records are for the same bus, false otherwise
     */
    private boolean isSameBus(InputRecord previous, InputRecord current) {
        return current.getBusId().equals(previous.getBusId());
    }

    /**
     * Checks if the current input record is on the same date as the previous input record.
     *
     * @param previous Previous input record
     * @param current Current input record
     * @return true if both records are on the same date, false otherwise
     */
    private boolean isSameDate(InputRecord previous, InputRecord current) {
        return previous.getDateTimeUTC().toLocalDate().equals(current.getDateTimeUTC().toLocalDate());
    }

    /**
     * Builds an output record based on the previous and current input records and the trip status.
     *
     * @param previous Previous input record
     * @param current Current input record
     * @param tripStatus Status of the trip (CANCELLED or COMPLETE)
     * @return Output record containing the processed information
     */
    private OutputRecord buildOutputRecord(InputRecord previous, InputRecord current, TripStatus tripStatus) {
        LOG.trace("Building output record");

        return switch (tripStatus) {
            case CANCELLED -> getCancelledTripOutputRecord(previous, current);
            case COMPLETE -> getCompleteTripOutputRecord(previous, current);
            case INCOMPLETE -> getIncompleteTripOutputRecord(previous);
        };
    }

    /**
     * Builds an output record for a cancelled trip.
     *
     * @param previous Previous input record
     * @param current Current input record
     * @return Output record for the cancelled trip
     */
    private OutputRecord getCancelledTripOutputRecord(InputRecord previous, InputRecord current) {
        LOG.trace("Cancelled trip output record");

        return OutputRecord.builder()
                .startUTC(previous.getDateTimeUTC())
                .finishUTC(current.getDateTimeUTC())
                .durationSecs(current.getDateTimeUTC().toEpochSecond(ZoneOffset.UTC) - previous.getDateTimeUTC().toEpochSecond(ZoneOffset.UTC))
                .fromStopId(previous.getStopId())
                .toStopId(current.getStopId())
                .chargeAmount(0.0D) // No charge for cancelled trips
                .companyId(previous.getCompanyId())
                .busId(previous.getBusId())
                .pan(previous.getPan())
                .tripStatus(TripStatus.CANCELLED)
                .build();
    }

    /**
     * Builds an output record for a complete trip.
     *
     * @param previous Previous input record
     * @param current Current input record
     * @return Output record for the complete trip
     */
    private OutputRecord getCompleteTripOutputRecord(InputRecord previous, InputRecord current) {
        LOG.trace("Completed trip output record");

        return OutputRecord.builder()
                .startUTC(previous.getDateTimeUTC())
                .finishUTC(current.getDateTimeUTC())
                .durationSecs(current.getDateTimeUTC().toEpochSecond(ZoneOffset.UTC) - previous.getDateTimeUTC().toEpochSecond(ZoneOffset.UTC))
                .fromStopId(previous.getStopId())
                .toStopId(current.getStopId())
                .chargeAmount(routeDataRepository.getChargeBetween(previous.getStopId(), current.getStopId()))
                .companyId(previous.getCompanyId())
                .busId(previous.getBusId())
                .pan(previous.getPan())
                .tripStatus(TripStatus.COMPLETE)
                .build();
    }

    /**
     * Builds an output record for an incomplete trip.
     *
     * @param previous Previous input record
     * @return Output record for the incomplete trip
     */
    private OutputRecord getIncompleteTripOutputRecord(InputRecord previous) {
        LOG.trace("Incomplete trip output record");

        OutputRecord record = OutputRecord.builder()
                .durationSecs(null)
                .fromStopId(previous.getStopId())
                .toStopId(null)
                .chargeAmount(stopDataRepository.getMaxRouteLegCostFromStop(previous.getStopId()))
                .companyId(previous.getCompanyId())
                .busId(previous.getBusId())
                .pan(previous.getPan())
                .tripStatus(TripStatus.INCOMPLETE)
                .build();

        // If the incomplete trip is tapped on, set start time
        if (previous.getTapType() == TapType.ON) {
            record.setStartUTC(previous.getDateTimeUTC());
            record.setFinishUTC(null);
        } else { // If tapped off, set finish time
            record.setStartUTC(null); // Start time is unknown for incomplete trips that are tapped off
            record.setFinishUTC(previous.getDateTimeUTC());
        }

        return record;
    }
}
