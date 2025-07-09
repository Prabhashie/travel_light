package com.littlepay.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * An entity class to store an inpuqt record
 *
 * @author Sachi
 */
@Data
@Builder
public class InputRecord {
    private int id;

    private LocalDateTime dateTimeUTC;

    private TapType tapType;

    private Stop stopId;

    private Company companyId;

    private Bus busId;

    private Long pan;
}
