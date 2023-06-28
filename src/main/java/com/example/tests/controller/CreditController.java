package com.example.tests.controller;

import com.example.tests.enums.ClientType;
import com.example.tests.service.CreditCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreditController {
    private final CreditCalculator creditCalculator;

    @GetMapping("/overpayment")
    public double getOverPayment() {
        return creditCalculator.calculateOverpayment(10000d, 500d, ClientType.INDIVIDUAL);
    }
}