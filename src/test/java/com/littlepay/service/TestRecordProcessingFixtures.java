package com.littlepay.service;

import com.littlepay.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestRecordProcessingFixtures {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm");
    public static final List<InputRecord> ALL_COMPLETE_INPUT = List.of(
            new InputRecord(1, LocalDateTime.parse("22/01/2023 13:00", FORMATTER), TapType.ON, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 1111111100000000L),
            new InputRecord(2, LocalDateTime.parse("22/01/2023 14:00", FORMATTER), TapType.OFF, new Stop("Stop2"), new Company("Company1"), new Bus("Bus11"), 1111111100000000L),
            new InputRecord(3, LocalDateTime.parse("23/01/2023 15:00", FORMATTER), TapType.ON, new Stop("Stop2"), new Company("Company1"), new Bus("Bus12"), 2222222200000000L),
            new InputRecord(4, LocalDateTime.parse("23/01/2023 16:00", FORMATTER), TapType.OFF, new Stop("Stop3"), new Company("Company1"), new Bus("Bus12"), 2222222200000000L)
    );

    public static final List<OutputRecord> ALL_COMPLETE_OUTPUT = List.of(
            new OutputRecord(LocalDateTime.parse("22/01/2023 13:00", FORMATTER), LocalDateTime.parse("22/01/2023 14:00", FORMATTER), 3600L, new Stop("Stop1"), new Stop("Stop2"), 3.25, new Company("Company1"), new Bus("Bus11"), 1111111100000000L, TripStatus.COMPLETE),
            new OutputRecord(LocalDateTime.parse("23/01/2023 15:00", FORMATTER), LocalDateTime.parse("23/01/2023 16:00", FORMATTER), 3600L, new Stop("Stop2"), new Stop("Stop3"), 5.5, new Company("Company1"), new Bus("Bus12"), 2222222200000000L, TripStatus.COMPLETE)
    );

    public static final List<InputRecord> ALL_INCOMPLETE_INPUT = List.of(
            new InputRecord(1, LocalDateTime.parse("22/01/2023 13:00", FORMATTER), TapType.ON, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 1111111100000000L),
            new InputRecord(2, LocalDateTime.parse("23/01/2023 14:00", FORMATTER), TapType.ON, new Stop("Stop2"), new Company("Company1"), new Bus("Bus12"), 1111111100000000L),
            new InputRecord(3, LocalDateTime.parse("24/01/2023 15:00", FORMATTER), TapType.OFF, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 2222222200000000L),
            new InputRecord(4, LocalDateTime.parse("25/01/2023 16:00", FORMATTER), TapType.OFF, new Stop("Stop2"), new Company("Company1"), new Bus("Bus12"), 2222222200000000L)
    );

    public static final List<OutputRecord> ALL_INCOMPLETE_OUTPUT = List.of(
            new OutputRecord(LocalDateTime.parse("22/01/2023 13:00", FORMATTER), null, null, new Stop("Stop1"), null, 7.30, new Company("Company1"), new Bus("Bus11"), 1111111100000000L, TripStatus.INCOMPLETE),
            new OutputRecord(LocalDateTime.parse("23/01/2023 14:00", FORMATTER), null, null, new Stop("Stop2"), null, 5.5, new Company("Company1"), new Bus("Bus12"), 1111111100000000L, TripStatus.INCOMPLETE),
            new OutputRecord(null, LocalDateTime.parse("24/01/2023 15:00", FORMATTER), null, new Stop("Stop1"), null, 7.3, new Company("Company1"), new Bus("Bus11"), 2222222200000000L, TripStatus.INCOMPLETE),
            new OutputRecord(null, LocalDateTime.parse("25/01/2023 16:00", FORMATTER), null, new Stop("Stop2"), null, 5.5, new Company("Company1"), new Bus("Bus12"), 2222222200000000L, TripStatus.INCOMPLETE)
    );

    public static final List<InputRecord> ALL_CANCELLED_INPUT = List.of(
            new InputRecord(1, LocalDateTime.parse("22/01/2023 13:00", FORMATTER), TapType.ON, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 1111111100000000L),
            new InputRecord(2, LocalDateTime.parse("22/01/2023 13:05", FORMATTER), TapType.OFF, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 1111111100000000L),
            new InputRecord(3, LocalDateTime.parse("23/01/2023 15:00", FORMATTER), TapType.ON, new Stop("Stop2"), new Company("Company1"), new Bus("Bus12"), 2222222200000000L),
            new InputRecord(4, LocalDateTime.parse("23/01/2023 15:05", FORMATTER), TapType.OFF, new Stop("Stop2"), new Company("Company1"), new Bus("Bus12"), 2222222200000000L)
    );

    public static final List<OutputRecord> ALL_CANCELLED_OUTPUT = List.of(
            new OutputRecord(LocalDateTime.parse("22/01/2023 13:00", FORMATTER), LocalDateTime.parse("22/01/2023 13:05", FORMATTER), 300L, new Stop("Stop1"), new Stop("Stop1"), 0.0, new Company("Company1"), new Bus("Bus11"), 1111111100000000L, TripStatus.CANCELLED),
            new OutputRecord(LocalDateTime.parse("23/01/2023 15:00", FORMATTER), LocalDateTime.parse("23/01/2023 15:05", FORMATTER), 300L, new Stop("Stop2"), new Stop("Stop2"), 0.0, new Company("Company1"), new Bus("Bus12"), 2222222200000000L, TripStatus.CANCELLED)
    );

    public static final List<InputRecord> COMBINED_INPUT = List.of(
            new InputRecord(1, LocalDateTime.parse("24/01/2023 15:00", FORMATTER), TapType.OFF, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 1111111100000000L),
            new InputRecord(2, LocalDateTime.parse("22/01/2023 13:00", FORMATTER), TapType.ON, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 2222222200000000L),
            new InputRecord(3, LocalDateTime.parse("22/01/2023 14:00", FORMATTER), TapType.OFF, new Stop("Stop2"), new Company("Company1"), new Bus("Bus11"), 2222222200000000L),
            new InputRecord(4, LocalDateTime.parse("22/01/2023 13:00", FORMATTER), TapType.ON, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 3333333300000000L),
            new InputRecord(5, LocalDateTime.parse("22/01/2023 13:05", FORMATTER), TapType.OFF, new Stop("Stop1"), new Company("Company1"), new Bus("Bus11"), 3333333300000000L),
            new InputRecord(6, LocalDateTime.parse("25/01/2023 16:00", FORMATTER), TapType.OFF, new Stop("Stop2"), new Company("Company1"), new Bus("Bus12"), 4444444400000000L)
    );

    public static final List<OutputRecord> COMBINED_OUTPUT = List.of(
            new OutputRecord(null, LocalDateTime.parse("24/01/2023 15:00", FORMATTER), null, new Stop("Stop1"), null, 7.3, new Company("Company1"), new Bus("Bus11"), 1111111100000000L, TripStatus.INCOMPLETE),
            new OutputRecord(LocalDateTime.parse("22/01/2023 13:00", FORMATTER), LocalDateTime.parse("22/01/2023 14:00", FORMATTER), 3600L, new Stop("Stop1"), new Stop("Stop2"), 3.25, new Company("Company1"), new Bus("Bus11"), 2222222200000000L, TripStatus.COMPLETE),
            new OutputRecord(LocalDateTime.parse("22/01/2023 13:00", FORMATTER), LocalDateTime.parse("22/01/2023 13:05", FORMATTER), 300L, new Stop("Stop1"), new Stop("Stop1"), 0.0, new Company("Company1"), new Bus("Bus11"), 3333333300000000L, TripStatus.CANCELLED),
            new OutputRecord(null, LocalDateTime.parse("25/01/2023 16:00", FORMATTER), null, new Stop("Stop2"), null, 5.5, new Company("Company1"), new Bus("Bus12"), 4444444400000000L, TripStatus.INCOMPLETE)
    );
}
