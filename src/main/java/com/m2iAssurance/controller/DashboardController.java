package com.m2iAssurance.controller;


import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import com.m2iAssurance.repository.UserRepository;
import com.m2iAssurance.service.PolicyService;
import com.m2iAssurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    UserService userService;

    @Autowired
    PolicyService policyService;

    @GetMapping
    public String dashboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get logged-in username
        User user = userService.getUserByUsername(username);
        Long active = policyService.activePoliciesCount(user);
        LocalDate nextPayment = policyService.nextPayment(user);
        BigDecimal totalPremium = policyService.getTotalPremium(user);
        List<Policy> last = policyService.getLastPolicies(user);

        model.addAttribute("active", active);
        model.addAttribute("nextPayment", nextPayment);
        model.addAttribute("totalPremium", totalPremium);
        model.addAttribute("last", last);
        model.addAttribute("username", username);
        return "dashboard";
    }



}
