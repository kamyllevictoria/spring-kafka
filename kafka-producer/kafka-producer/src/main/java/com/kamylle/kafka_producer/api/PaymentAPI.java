package com.kamylle.kafka_producer.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kamylle.kafka_producer.dto.PaymentDTO;
import com.kamylle.kafka_producer.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentAPI {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public String pay(@RequestBody PaymentDTO payment) throws JsonProcessingException {
        return paymentService.integratePayment(payment);
    }

    //getting payment by number
    @GetMapping("/{number}")
    public ResponseEntity<PaymentDTO>getPaymentByNumber(@PathVariable Integer number){
        try{
            PaymentDTO payment = paymentService.getPaymentByNumber(number);
            if(payment != null){
                return ResponseEntity.ok(payment);
            } else{
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //get payment details
    @GetMapping("/stats")
    public ResponseEntity<List<PaymentDTO>>getPaymentDetails(){
        try{
            List<PaymentDTO> payments = paymentService.getPaymentsDetails();
            return ResponseEntity.ok(payments);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //get payment status
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(){
        Map<String, Object> status = new HashMap<>();
        status.put("service", "Payment Producer");
        status.put("status", "Running");
        status.put("timestamp", LocalDateTime.now());
        status.put("totalPayments", paymentService.getTotalPayments());
        return ResponseEntity.ok(status);
    }
}
