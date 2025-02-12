package com.m2iAssurance.controller;

import com.m2iAssurance.enums.PolicyStatus;
import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import com.m2iAssurance.service.PolicyService;
import com.m2iAssurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    PolicyService policyService;

    @Autowired
    UserService userService;

    @GetMapping("/adminDashboard")
    public String admintDashboard(Model model){
        Long active = policyService.policyCount("ACTIVE");
        Long pending = policyService.policyCount("PENDING");
        BigDecimal premium = policyService.getAllTotalPremium();

        List<Policy> activePolicies = policyService.getRecentPoliciesByStatus("ACTIVE");
        List<Policy> pendingPolicies = policyService.getRecentPoliciesByStatus("PENDING");
        Page<User> recentUsers = userService.getRecentUsers();

        model.addAttribute("activePolicies", activePolicies);
        model.addAttribute("pendingPolicies", pendingPolicies);
        model.addAttribute("recentUsers", recentUsers);

        model.addAttribute("active", active);
        model.addAttribute("pending", pending);
        model.addAttribute("premium", premium);

        return "adminDashboard";
    }


    @GetMapping("/pendingPolicies")
    public String pendingPolicies(Model model){
        Long active = policyService.policyCount("ACTIVE");
        Long pending = policyService.policyCount("PENDING");
        Long totalPolicies = active + pending;

        List<Policy> policies = policyService.getPoliciesByStatus("PENDING");


        model.addAttribute("active", active);
        model.addAttribute("pending", pending);
        model.addAttribute("totalPolicies", totalPolicies);
        model.addAttribute("policies", policies);


        return "pendingPolicies";
    }

    @GetMapping("/activePolicies")
    public String activePolicies(Model model){
        Long active = policyService.policyCount("ACTIVE");
        Long pending = policyService.policyCount("PENDING");
        Long totalPolicies = active + pending;

        List<Policy> policies = policyService.getPoliciesByStatus("ACTIVE");


        model.addAttribute("active", active);
        model.addAttribute("pending", pending);
        model.addAttribute("totalPolicies", totalPolicies);
        model.addAttribute("policies", policies);


        return "activePolicies";
    }


    @GetMapping("/clients")
    public String clients(Model model){

        List<User> clients = userService.getAllUsers();
        model.addAttribute("clients", clients);

        return "clients";
    }

    @GetMapping("/policyDetails")
    public String policyDetails(@RequestParam String policyNumber, Model model) {
        Policy policy = policyService.getPolicyByNumber(policyNumber);
        model.addAttribute("policy", policy);
        return "adminPolicyDetails";
    }

    @Controller
    public class PolicyController {

        @Autowired
        private PolicyService policyService;

        @GetMapping("/approve")
        public String approvePolicy(@RequestParam String policyNumber, RedirectAttributes redirectAttributes) {

                Policy policy = policyService.getPolicyByNumber(policyNumber);

                policy.setStatus(PolicyStatus.ACTIVE);
                policyService.save(policy);

                redirectAttributes.addFlashAttribute("success", "Policy " + policyNumber + " has been approved successfully");
                return "redirect:/pendingPolicies";

        }

        @GetMapping("/adminPersonalInfo")
        public String personlaInfo(Model model, Principal principal){

            String username = principal.getName();

            User user = userService.getUserByUsername(username);
            model.addAttribute("user", user);

            return "adminPersonalInfo";
        }

        @PostMapping("/adminPersonalInfo/update/{username}")
        public String updateUser(@PathVariable("username") String username,
                                 User user,
                                 Model model) {

            user = userService.updateUser(username, user);
            model.addAttribute("user", user);

            return "redirect:/adminPersonalInfo";

        }

        @PostMapping("/clients/delete")
        public String delete(@RequestParam String id) {
            userService.deleteUser(id);
            return "redirect:/clients";
        }

        @GetMapping("/clientDetails")
        public String clientDetails(@RequestParam String id, Model model) {
            User user = userService.getUserById(id);
            model.addAttribute("client", user);
            return "clientDetails";
        }
    }


}
