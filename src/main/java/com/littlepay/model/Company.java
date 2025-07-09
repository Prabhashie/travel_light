package com.littlepay.model;

import lombok.Builder;
import lombok.Data;

/**
 * An entity class to store a company
 *
 * @author Sachi
 */
@Data
@Builder
public class Company {

    private String companyId;

    public Company(String companyId) {
        this.companyId = companyId;
    }
}
