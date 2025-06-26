package com.kamylle.kafka_producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kamylle.kafka_producer.dto.PaymentDTO;
import com.kamylle.kafka_producer.producer.PaymentRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class PaymentService {
    @Autowired
    private PaymentRequestProducer paymentRequestProducer;

    //savind data at the memory to simulate a database
    private Map<Integer, PaymentDTO> paymentsDataBase = new ConcurrentHashMap<>();

    public String integratePayment(PaymentDTO payment) throws JsonProcessingException {
        if (payment == null || payment.getNumber() == null) {
            return "Invalid payment data provided";
        }

        if (paymentsDataBase.containsKey(payment.getNumber())) {
            return "Payment with number " + payment.getNumber() + " already exists";
        }

        try {
            paymentRequestProducer.sendMessge(payment);
            paymentsDataBase.put(payment.getNumber(), payment);
            return String.format("Payment request send successfully! Number: %d, Amount: %.2f", payment.getNumber(), payment.getValue());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "There was an error to process the payment:" +e.getMessage();

        } catch (Exception e){
            e.printStackTrace();
            return "Unexpected error ocurred while processing payment:" +e.getMessage();
        }
    }

    public PaymentDTO getPaymentByNumber(Integer number){
        return paymentsDataBase.get(number);
    }

    public ArrayList<PaymentDTO> getPaymentsDetails(){
        return new ArrayList<>(paymentsDataBase.values());
    }

    public int getTotalPayments(){
        return paymentsDataBase.size();
    }

    private BigDecimal calculateTotalValue(){
        return paymentsDataBase.values().stream()
                .map(PaymentDTO::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    //complemmentar statistics methods
    public Map<String, Object> getPaymentsStatistics(){
        Map<String, Object> stats  = new HashMap<>();
        List<PaymentDTO> payments = new ArrayList<>(paymentsDataBase.values());

        stats.put("totalCount", paymentsDataBase.size());
        stats.put("totalValue", calculateTotalValue());

        if(!payments.isEmpty()){
            stats.put("averageValue", getAverage());
            stats.put("highestValue", getHighestPayment());
            stats.put("lowestValue", getLowestPayment());
        }

        stats.put("payments", payments);
        return stats;
    }

    private BigDecimal getAverage(){
        if(paymentsDataBase.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = calculateTotalValue();
        return total.divide(BigDecimal.valueOf(paymentsDataBase.size()),2, RoundingMode.HALF_UP);
    }

    private BigDecimal getHighestPayment(){
        return paymentsDataBase.values().stream()
                .map(PaymentDTO::getValue)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getLowestPayment(){
        return paymentsDataBase.values().stream()
                .map(PaymentDTO::getValue)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
