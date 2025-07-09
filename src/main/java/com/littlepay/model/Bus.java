package com.littlepay.model;

import lombok.Builder;
import lombok.Data;

/**
 * An entity class to store a bus
 *
 * @author Sachi
 */
@Data
@Builder
public class Bus {

    private String busId;

    public Bus(String busId) {
        this.busId = busId;
    }
}
