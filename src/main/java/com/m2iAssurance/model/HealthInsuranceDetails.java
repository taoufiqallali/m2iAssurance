package com.m2iAssurance.model;

import com.m2iAssurance.enums.CoverageLevel;
import lombok.Data;

import java.util.List;

@Data
public class HealthInsuranceDetails {
    private Boolean hasPreviousConditions;
    private CoverageLevel desiredCoverage;
    private Boolean includesDental;
    private Boolean includesVision;
}