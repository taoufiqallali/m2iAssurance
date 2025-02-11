package com.m2iAssurance.controller;


import com.m2iAssurance.enums.PolicyCategory;
import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import com.m2iAssurance.repository.UserRepository;
import com.m2iAssurance.service.PolicyService;
import com.m2iAssurance.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
@Data
public class CategoriesController {

    @Autowired
    UserService userService;

    @Autowired
    PolicyService policyService;

    @GetMapping("/life")
    public String life(Model model){

        Policy policy = new Policy();
        model.addAttribute("policy", policy);

        return "life";
    }

    @PostMapping("/life")
    public String submitLifeInsurance(@ModelAttribute Policy policy, Principal principal) {
        String username = principal.getName();
        policy.setPolicyholder(userService.getUserByUsername(username));
        policy.setCategory(PolicyCategory.Life);
        policyService.submitApplication(policy);
        return "redirect:/policies/myPolicies"; // Redirect after successful submission
    }


    @GetMapping("/vehicule")
    public String vehicule(Model model){

        Policy policy = new Policy();
        model.addAttribute("policy", policy);

        return "vehicule";
    }

    @PostMapping("/vehicule")
    public String submitVehiculeInsurance(@ModelAttribute Policy policy, Principal principal) {
        String username = principal.getName();
        policy.setPolicyholder(userService.getUserByUsername(username));
        policy.setCategory(PolicyCategory.Vehicle);
        policyService.submitApplication(policy);
        return "redirect:/policies/myPolicies"; // Redirect after successful submission
    }

    @GetMapping("/property")
    public String property(Model model){

        Policy policy = new Policy();
        model.addAttribute("policy", policy);

        return "property";
    }

    @PostMapping("/property")
    public String submitPropertyInsurance(@ModelAttribute Policy policy, Principal principal) {
        String username = principal.getName();
        policy.setPolicyholder(userService.getUserByUsername(username));
        policy.setCategory(PolicyCategory.Property);
        policyService.submitApplication(policy);
        return "redirect:/policies/myPolicies"; // Redirect after successful submission
    }

    @GetMapping("/health")
    public String health(Model model){

        Policy policy = new Policy();
        model.addAttribute("policy", policy);

        return "health";
    }

    @PostMapping("/health")
    public String submitHealthInsurance(@ModelAttribute Policy policy, Principal principal) {
        String username = principal.getName();
        policy.setPolicyholder(userService.getUserByUsername(username));
        policy.setCategory(PolicyCategory.Health);
        policyService.submitApplication(policy);
        return "redirect:/policies/myPolicies"; // Redirect after successful submission
    }

    @GetMapping("/business")
    public String business(Model model){

        Policy policy = new Policy();
        model.addAttribute("policy", policy);

        return "business";
    }

    @PostMapping("/business")
    public String submitBusinessInsurance(@ModelAttribute Policy policy, Principal principal) {
        String username = principal.getName();
        policy.setPolicyholder(userService.getUserByUsername(username));
        policy.setCategory(PolicyCategory.Business);
        policyService.submitApplication(policy);
        return "redirect:/policies/myPolicies"; // Redirect after successful submission
    }

}
