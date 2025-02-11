package com.m2iAssurance.model;

import com.m2iAssurance.enums.BusinessCoverage;
import com.m2iAssurance.enums.IndustryType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BusinessInsuranceDetails {
    private String businessName;
    private Integer numberOfEmployees;
    private BigDecimal annualRevenue;
    private IndustryType industryType;
    private Integer yearsInBusiness;
}
