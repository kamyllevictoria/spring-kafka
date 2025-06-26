package com.kamylle.kafka_producer.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kamylle.kafka_producer.dto.PaymentDTO;
import com.kamylle.kafka_producer.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentAPI {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public String pay(@RequestBody PaymentDTO payment) throws JsonProcessingException {
        return paymentService.integratePayment(payment);
    }
}
