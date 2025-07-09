package com.littlepay.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * An entity class to store an output record
 *
 * @author Sachi
 */
@Data
@Builder
public class OutputRecord {

    private LocalDateTime startUTC;

    private LocalDateTime finishUTC;

    private long durationSecs;

    private Stop fromStopId;

    private Stop toStopId;

    private double chargeAmount;

    private Company companyId;

    private Bus busId;

    private Long pan;

    private TripStatus tripStatus;
}
