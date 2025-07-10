package com.littlepay.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An entity class to store an input record
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

    public InputRecord(int id, LocalDateTime dateTimeUTC, TapType tapType, Stop stopId, Company companyId, Bus busId, Long pan) {
        this.id = id;
        this.dateTimeUTC = dateTimeUTC;
        this.tapType = tapType;
        this.stopId = stopId;
        this.companyId = companyId;
        this.busId = busId;
        this.pan = pan;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InputRecord that = (InputRecord) o;
        return id == that.id &&
                Objects.equals(dateTimeUTC, that.dateTimeUTC) &&
                tapType == that.tapType &&
                Objects.equals(stopId, that.stopId) &&
                Objects.equals(companyId, that.companyId) &&
                Objects.equals(busId, that.busId) &&
                Objects.equals(pan, that.pan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTimeUTC, tapType, stopId, companyId, busId, pan);
    }
}
