package com.littlepay.service;

import com.littlepay.model.InputRecord;
import com.littlepay.model.OutputRecord;
import com.littlepay.model.TapType;
import com.littlepay.model.TripStatus;
import com.littlepay.repository.RouteDataRepository;
import com.littlepay.repository.StopDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RouteDataRepository routeDataRepository;

    @Autowired
    private StopDataRepository stopDataRepository;

    /**
     * Processes a list of input records and generates output records.
     *
     * @param inputRecords List of input records to process
     * @return List of processed output records
     */
    public List<OutputRecord> processRecords(List<InputRecord> inputRecords) {
        LOG.info("Processing {} input records", inputRecords.size());

        if (inputRecords == null || inputRecords.isEmpty()) {
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
     * @param groupedRecords Map of PAN to list of input records
     * @return List of output records
     */
    private List<OutputRecord> generateOutputRecords(Map<Long, List<InputRecord>> groupedRecords) {
        LOG.info("Generating output records from grouped input records");

        List<OutputRecord> outputRecords = new ArrayList<>();

        for (Map.Entry<Long, List<InputRecord>> entry : groupedRecords.entrySet()) {
            Long pan = entry.getKey();
            List<InputRecord> records = entry.getValue();

            int previousIndex = 0;
            int currentIndex = 1;

            while (previousIndex < records.size() && currentIndex < records.size()) {
                InputRecord previous = records.get(previousIndex);
                InputRecord current = records.get(currentIndex);
                LOG.trace("Processing record for PAN: {}, DateTimeUTC: {}, TapType: {}, Stop: {}, Bus: {}",
                        pan, current.getDateTimeUTC(), current.getTapType(), current.getStopId(), current.getBusId());

                // Previous tap was ON, check the current tap
                if (previous.getTapType().equals(TapType.ON)) {
                    // Current tap is OFF
                    if (current.getTapType().equals(TapType.OFF)) {
                        // Current tap in on the same bus
                        if (current.getBusId().equals(previous.getBusId())) {
                            // Tapping at the same stop -> Cancelled trip
                            if (current.getStopId().equals(previous.getStopId())) {
                                outputRecords.add(buildOutputRecord(previous, current, TripStatus.CANCELLED));

                                // Move to the next pair of records
                                previousIndex += 2;
                                currentIndex += 2;
                            } else { // Tapping at different stops -> Complete trip
                                // If the stop is different, we build an output record
                                outputRecords.add(buildOutputRecord(previous, current, TripStatus.COMPLETE));

                                // Move to the next pair of records
                                previousIndex += 2;
                                currentIndex += 2;
                            }
                        }
                    } else { // Consecutive ON taps -> Incomplete trip
                        outputRecords.add(buildOutputRecord(previous, null, TripStatus.INCOMPLETE));
                        previousIndex++;
                        currentIndex++;
                    }
                } else {
                    previousIndex++;
                    currentIndex++;
                }
            }
        }

        return outputRecords;
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
        return switch (tripStatus) {
            case CANCELLED -> cancelledTripOutputRecord(previous, current);
            case COMPLETE -> completeTripOutputRecord(previous, current);
            case INCOMPLETE -> incompleteTripOutputRecord(previous);
        };
    }

    /**
     * Builds an output record for a cancelled trip.
     *
     * @param previous Previous input record
     * @param current Current input record
     * @return Output record for the cancelled trip
     */
    private OutputRecord cancelledTripOutputRecord(InputRecord previous, InputRecord current) {
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
    private OutputRecord completeTripOutputRecord(InputRecord previous, InputRecord current) {
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

    private OutputRecord incompleteTripOutputRecord(InputRecord previous) {
        return OutputRecord.builder()
                .startUTC(previous.getDateTimeUTC())
                .finishUTC(null)
                .durationSecs(null)
                .fromStopId(previous.getStopId())
                .toStopId(null)
                .chargeAmount(stopDataRepository.getMaxRouteLegCostFromStop(previous.getStopId()))
                .companyId(previous.getCompanyId())
                .busId(previous.getBusId())
                .pan(previous.getPan())
                .tripStatus(TripStatus.INCOMPLETE)
                .build();
    }
}
