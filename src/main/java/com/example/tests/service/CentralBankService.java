package com.example.tests.service;

import com.example.tests.exception.CentralBankNotRespondingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CentralBankService {


    private final RestTemplate restTemplate;

    /**
     * Возвращает ключевую ставку Центрального банка
     **/
    public double getKeyRate() {
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity("http://web.cbr.ru/DailyInfo/rate", String.class);
        } catch (Exception e) {
            throw new CentralBankNotRespondingException();
        }
        return Double.parseDouble(response.getBody());
    }

    public double getDefaultCreditRate() {
        return 30d;
    }
}