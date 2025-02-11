package com.m2iAssurance.repository;

import com.m2iAssurance.enums.PolicyCategory;
import com.m2iAssurance.enums.PolicyStatus;
import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends MongoRepository<Policy, String> {
    List<Policy> findByCategory(PolicyCategory category);
    List<Policy> findByStatus(PolicyStatus status);
    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByPolicyholder_Id(String policyHolderId);
    List<Policy> findByPolicyholder_Username(String policyHolderId);
    List<Policy> findByPolicyholder(User policyholder);
    List<Policy> findByPolicyholderAndStatus(User user, String status);
    Long countByPolicyholderAndStatus(User user, String status);
    List<Policy> findByPolicyholder(User policyholder, Pageable pageable);
    Long countByPolicyholder(User user);
    void deleteByPolicyNumber(String policyNumber);

}

