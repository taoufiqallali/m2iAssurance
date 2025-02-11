package com.m2iAssurance.controller;

import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import com.m2iAssurance.service.PolicyService;
import com.m2iAssurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/policies")
public class PoliciesController {

    @Autowired
    UserService userService;

    @Autowired
    PolicyService policyService;

    @GetMapping("/categories")
    public String categories(){
        return "categories";
    }

    @GetMapping("/myPolicies")
    public String myPolicies(Model model, Principal principal){
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        List<Policy> policies = policyService.getPoliciesByPolicyHolder(user);
        Long pending = policyService.pendingPoliciesCount(user);
        Long active = policyService.activePoliciesCount(user);
        Long totalPolices = policyService.totalPoliciesCount(user);
        model.addAttribute("pending", pending);
        model.addAttribute("active", active);
        model.addAttribute("totalPolicies", totalPolices);
        model.addAttribute("policies", policies);

        return "myPolicies";
    }

    @GetMapping("/policyDetails")
    public String policyDetails(@RequestParam String policyNumber, Model model) {
        Policy policy = policyService.getPolicyByNumber(policyNumber);
        model.addAttribute("policy", policy);
        return "policyDetails";
    }


}
