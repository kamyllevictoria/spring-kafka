package com.kamylle.kafka_producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kamylle.kafka_producer.dto.PaymentDTO;
import com.kamylle.kafka_producer.producer.PaymentRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {
    @Autowired private PaymentRequestProducer paymentRequestProducer;

    public String integratePayment(PaymentDTO payment) throws JsonProcessingException {
        try{
            paymentRequestProducer.sendMessge(payment);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "There was an error to solicit the payment.";
    }

}
