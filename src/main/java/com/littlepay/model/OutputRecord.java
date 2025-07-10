package com.littlepay.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

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

    private Long durationSecs; // duration can be null for incomplete trips

    private Stop fromStopId;

    private Stop toStopId;

    private double chargeAmount;

    private Company companyId;

    private Bus busId;

    private Long pan;

    private TripStatus tripStatus;

    public OutputRecord(LocalDateTime startUTC, LocalDateTime finishUTC, Long durationSecs, Stop fromStopId, Stop toStopId, double chargeAmount, Company companyId, Bus busId, Long pan, TripStatus tripStatus) {
        this.startUTC = startUTC;
        this.finishUTC = finishUTC;
        this.durationSecs = durationSecs;
        this.fromStopId = fromStopId;
        this.toStopId = toStopId;
        this.chargeAmount = chargeAmount;
        this.companyId = companyId;
        this.busId = busId;
        this.pan = pan;
        this.tripStatus = tripStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OutputRecord that = (OutputRecord) o;
        return Double.compare(chargeAmount, that.chargeAmount) == 0 &&
                Objects.equals(startUTC, that.startUTC) &&
                Objects.equals(finishUTC, that.finishUTC) &&
                Objects.equals(durationSecs, that.durationSecs) &&
                Objects.equals(fromStopId, that.fromStopId) &&
                Objects.equals(toStopId, that.toStopId) &&
                Objects.equals(companyId, that.companyId) &&
                Objects.equals(busId, that.busId) &&
                Objects.equals(pan, that.pan) &&
                tripStatus == that.tripStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startUTC, finishUTC, durationSecs, fromStopId, toStopId, chargeAmount, companyId, busId, pan, tripStatus);
    }
}
