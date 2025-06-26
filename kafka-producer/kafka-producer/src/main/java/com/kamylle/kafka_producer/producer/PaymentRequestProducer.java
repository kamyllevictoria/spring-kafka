package com.kamylle.kafka_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamylle.kafka_producer.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestProducer {
    @Value("${topicos.pagamento.request.topic}")
    private String topicPaymentRequest;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessge(PaymentDTO payment) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(payment);
         // você precisa retornar algo se o método é do tipo String
        kafkaTemplate.send(topicPaymentRequest, content);
        return "Payment sent to processing";
    }
}
