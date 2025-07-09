package com.littlepay.model;

import lombok.Builder;
import lombok.Data;

/**
 * An entity class to store a stop
 *
 * @author Sachi
 */
@Data
@Builder
public class Stop {

    private String stopId;

    public Stop(String stopId) {
        this.stopId = stopId;
    }

    public int getNumericId() {
        return Integer.parseInt(stopId.split("Stop")[1]);
    }
}

