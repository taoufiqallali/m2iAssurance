package com.m2iAssurance.model;

import com.m2iAssurance.enums.PolicyCategory;
import com.m2iAssurance.enums.PolicyStatus;
import com.m2iAssurance.model.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;



@Data
@Document(collection = "policies")
public class Policy {
    @Id
    private String id;
    private String policyNumber;
    private PolicyCategory category;
    private PolicyStatus status = PolicyStatus.PENDING;
    private BigDecimal coverageAmount;
    private BigDecimal monthlyPremium;
    private LocalDate startDate;
    private LocalDate endDate;

    // Reference to User entity
    @DBRef
    private User policyholder;

    // Category Specific Details
    private LifeInsuranceDetails lifeDetails;
    private VehicleInsuranceDetails vehicleDetails;
    private PropertyInsuranceDetails propertyDetails;
    private HealthInsuranceDetails healthDetails;
    private BusinessInsuranceDetails businessDetails;

    // Metadata
    private LocalDate submissionDate = LocalDate.now();


}
