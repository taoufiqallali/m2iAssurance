package com.m2iAssurance.service;

import com.m2iAssurance.enums.IndustryType;
import com.m2iAssurance.enums.PolicyCategory;
import com.m2iAssurance.enums.PolicyStatus;
import com.m2iAssurance.model.LifeInsuranceDetails;
import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import  com.m2iAssurance.repository.PolicyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;

    public Policy submitApplication(Policy policy) {
        policy.setPolicyNumber(generatePolicyNumber(policy.getCategory()));
        policy.setMonthlyPremium(calculatePremium(policy));
        return policyRepository.save(policy);
    }

    public Policy updatePolicyStatus(String policyNumber, PolicyStatus newStatus) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        policy.setStatus(newStatus);
        return policyRepository.save(policy);
    }

    private BigDecimal calculatePremium(Policy policy) {
        return switch (policy.getCategory()) {
            case Life -> calculateLifePremium(policy);
            case Vehicle -> calculateVehiclePremium(policy);
            case Property -> calculatePropertyPremium(policy);
            case Health -> calculateHealthPremium(policy);
            case Business -> calculateBusinessPremium(policy);
        };
    }

    private BigDecimal calculateLifePremium(Policy policy) {
        BigDecimal basePremium = policy.getCoverageAmount().multiply(new BigDecimal("0.001"));
        LifeInsuranceDetails details = policy.getLifeDetails();

        // Age factor
        int age = calculateAge(policy.getPolicyholder().getDateOfBirth());
        if (age > 50) basePremium = basePremium.multiply(new BigDecimal("1.5"));

        // Medical history factor
        if (details.getPreExistingConditions() != null && !details.getPreExistingConditions().isEmpty()) {
            basePremium = basePremium.multiply(new BigDecimal("1.3"));
        }

        return basePremium;
    }

    private String generatePolicyNumber(PolicyCategory category) {
        return String.format("%s-%s-%d",
                category.toString().substring(0, 2),
                LocalDate.now().getYear(),
                System.currentTimeMillis() % 10000
        );
    }

    public static int calculateAge(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthLocalDate = LocalDate.parse(birthDate, formatter);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthLocalDate, currentDate).getYears();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    public static BigDecimal calculateVehiclePremium(Policy policy) {
        BigDecimal basePremium = BigDecimal.valueOf(500); // Base premium

        // Adjust premium based on vehicle age
        int currentYear = Year.now().getValue();
        int vehicleAge = currentYear - policy.getVehicleDetails().getVehicleYear().getYear();
        if (vehicleAge > 10) {
            basePremium = basePremium.add(BigDecimal.valueOf(200)); // Older vehicles cost more
        } else if (vehicleAge < 5) {
            basePremium = basePremium.subtract(BigDecimal.valueOf(100)); // Newer vehicles get a discount
        }

        // Adjust based on annual mileage
        if (policy.getVehicleDetails().getAnnualMileage() > 20000) {
            basePremium = basePremium.add(BigDecimal.valueOf(150)); // High mileage risk
        } else if (policy.getVehicleDetails().getAnnualMileage() < 5000) {
            basePremium = basePremium.subtract(BigDecimal.valueOf(100)); // Low mileage discount
        }

        // Commercial use surcharge
        if (Boolean.TRUE.equals(policy.getVehicleDetails().getIsCommercialUse())) {
            basePremium = basePremium.add(BigDecimal.valueOf(250)); // Commercial vehicles have more exposure
        }

        // Ensure minimum premium amount
        return basePremium.max(BigDecimal.valueOf(300));
    }

    public static BigDecimal calculatePropertyPremium(Policy policy) {
        BigDecimal basePremium = BigDecimal.valueOf(800); // Base premium

        // Adjust premium based on property age
        if (policy.getPropertyDetails().getPropertyAge() > 30) {
            basePremium = basePremium.add(BigDecimal.valueOf(300)); // Older properties cost more
        } else if (policy.getPropertyDetails().getPropertyAge() < 10) {
            basePremium = basePremium.subtract(BigDecimal.valueOf(150)); // Newer properties get a discount
        }

        // Adjust based on property size
        if (policy.getPropertyDetails().getPropertySize() > 200) {
            basePremium = basePremium.add(BigDecimal.valueOf(250)); // Large property = higher cost
        } else if (policy.getPropertyDetails().getPropertySize() < 50) {
            basePremium = basePremium.subtract(BigDecimal.valueOf(100)); // Small property = lower cost
        }

        // Apply discount if a security system is installed
        if (Boolean.TRUE.equals(policy.getPropertyDetails().getHasSecuritySystem())) {
            basePremium = basePremium.subtract(BigDecimal.valueOf(200)); // Security reduces risk
        }

        // Adjust based on property type
        switch (policy.getPropertyDetails().getPropertyType()) {
            case RESIDENTIAL:
                basePremium = basePremium.add(BigDecimal.valueOf(200));
                break;
            case RENTAL:
                basePremium = basePremium.subtract(BigDecimal.valueOf(100));
                break;
            case COMMERCIAL:
                basePremium = basePremium.add(BigDecimal.valueOf(500));
                break;
            default:
                break;
        }

        // Ensure minimum premium amount
        return basePremium.max(BigDecimal.valueOf(500));
    }

    public static BigDecimal calculateHealthPremium(Policy policy) {
        BigDecimal basePremium = BigDecimal.valueOf(300); // Base premium

        // Increase premium if the person has pre-existing conditions
        if (Boolean.TRUE.equals(policy.getHealthDetails().getHasPreviousConditions())) {
            basePremium = basePremium.add(BigDecimal.valueOf(200));
        }

        // Adjust premium based on coverage level
        switch (policy.getHealthDetails().getDesiredCoverage()) {
            case BASIC:
                basePremium = basePremium.add(BigDecimal.valueOf(100));
                break;
            case STANDARD:
                basePremium = basePremium.add(BigDecimal.valueOf(200));
                break;
            case PREMIUM:
                basePremium = basePremium.add(BigDecimal.valueOf(400));
                break;
            default:
                break;
        }

        // Add cost for dental and vision coverage
        if (Boolean.TRUE.equals(policy.getHealthDetails().getIncludesDental())) {
            basePremium = basePremium.add(BigDecimal.valueOf(50));
        }
        if (Boolean.TRUE.equals(policy.getHealthDetails().getIncludesVision())) {
            basePremium = basePremium.add(BigDecimal.valueOf(50));
        }

        // Ensure minimum premium amount
        return basePremium.max(BigDecimal.valueOf(350));
    }

    // Similar calculation methods for other categories...

    // Industry risk multipliers
    private static final EnumMap<IndustryType, BigDecimal> INDUSTRY_RISK_FACTORS = new EnumMap<>(IndustryType.class);

    static {
        INDUSTRY_RISK_FACTORS.put(IndustryType.CONSTRUCTION, BigDecimal.valueOf(1.5));  // High risk
        INDUSTRY_RISK_FACTORS.put(IndustryType.TECHNOLOGY, BigDecimal.valueOf(1.1));    // Low risk
        INDUSTRY_RISK_FACTORS.put(IndustryType.RETAIL, BigDecimal.valueOf(1.2));        // Medium risk
        INDUSTRY_RISK_FACTORS.put(IndustryType.HEALTHCARE, BigDecimal.valueOf(1.3));    // Medium-high risk
    }

    public static BigDecimal calculateBusinessPremium(Policy policy) {
        BigDecimal basePremium = BigDecimal.valueOf(1000); // Base premium

        // Adjust for number of employees
        if (policy.getBusinessDetails().getNumberOfEmployees() > 50) {
            basePremium = basePremium.add(BigDecimal.valueOf(500));
        } else if (policy.getBusinessDetails().getNumberOfEmployees() > 10) {
            basePremium = basePremium.add(BigDecimal.valueOf(200));
        }

        // Adjust for annual revenue
        if (policy.getBusinessDetails().getAnnualRevenue().compareTo(BigDecimal.valueOf(1_000_000)) > 0) {
            basePremium = basePremium.add(BigDecimal.valueOf(700));
        } else if (policy.getBusinessDetails().getAnnualRevenue().compareTo(BigDecimal.valueOf(500_000)) > 0) {
            basePremium = basePremium.add(BigDecimal.valueOf(400));
        }

        // Adjust based on industry type risk factor
        BigDecimal industryMultiplier = INDUSTRY_RISK_FACTORS.getOrDefault(policy.getBusinessDetails().getIndustryType(), BigDecimal.ONE);
        basePremium = basePremium.multiply(industryMultiplier);

        // New businesses (<3 years) pay higher premiums
        if (policy.getBusinessDetails().getYearsInBusiness() < 3) {
            basePremium = basePremium.add(BigDecimal.valueOf(500));
        }

        // Ensure a minimum premium
        return basePremium.max(BigDecimal.valueOf(1200));
    }

    public List<Policy> getPoliciesByPolicyHolderId(String id){
        return policyRepository.findByPolicyholder_Id(id);
    }

    public List<Policy> getPoliciesByPolicyHolderUsername(String username){
        return policyRepository.findByPolicyholder_Username(username);
    }

    public List<Policy> getPoliciesByPolicyHolder(User policyolder){
        return policyRepository.findByPolicyholder(policyolder);
    }

    public Long pendingPoliciesCount(User user){
        return policyRepository.countByPolicyholderAndStatus(user, "PENDING");
    }

    public Long activePoliciesCount(User user){
        return policyRepository.countByPolicyholderAndStatus(user, "ACTIVE");
    }

    public Long totalPoliciesCount(User user){
        return policyRepository.countByPolicyholder(user);
    }

    public LocalDate nextPayment(User user){
        List<Policy> policies = policyRepository.findByPolicyholder(user);
        return policies.stream()
                .map(Policy::getEndDate)
                .filter(date -> date.isAfter(LocalDate.now()))
                .min(LocalDate::compareTo).orElse(null);
    }

    public BigDecimal getTotalPremium(User user){
        List<Policy> policies = policyRepository.findByPolicyholderAndStatus(user, "ACTIVE");
        return policies.stream()
                .map(Policy::getMonthlyPremium)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Policy> getLastPolicies(User user){
        return policyRepository.findByPolicyholder(user, PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "endDate")));
    }

    public Policy getPolicyByNumber(String policyNumber){
        return policyRepository.findByPolicyNumber(policyNumber).orElse(null);
    }

    public Policy updatePolicy(Policy updatedPolicy) {
        return policyRepository.findByPolicyNumber(updatedPolicy.getPolicyNumber())
                .map(existingPolicy -> {
                    existingPolicy.setCoverageAmount(updatedPolicy.getCoverageAmount());
                    existingPolicy.setMonthlyPremium(updatedPolicy.getMonthlyPremium());
                    existingPolicy.setStartDate(updatedPolicy.getStartDate());
                    existingPolicy.setEndDate(updatedPolicy.getEndDate());
                    existingPolicy.setLifeDetails(updatedPolicy.getLifeDetails());
                    existingPolicy.setVehicleDetails(updatedPolicy.getVehicleDetails());
                    existingPolicy.setPropertyDetails(updatedPolicy.getPropertyDetails());
                    existingPolicy.setHealthDetails(updatedPolicy.getHealthDetails());
                    existingPolicy.setBusinessDetails(updatedPolicy.getBusinessDetails());
                    return policyRepository.save(existingPolicy);
                })
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + updatedPolicy.getId()));
    }

    public void deletePolicy(String policyNumber){

        policyRepository.deleteByPolicyNumber(policyNumber);

    }
}
