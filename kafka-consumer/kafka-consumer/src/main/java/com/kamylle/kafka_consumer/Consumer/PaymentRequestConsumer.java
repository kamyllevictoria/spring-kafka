package com.kamylle.kafka_consumer.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestConsumer {

    @KafkaListener(
            topics = "${topicos.pagamento.request.topic}",
            groupId = "pagamento-request-consumer-1"
    )
    public void consume(String message){
        System.out.println("==== Received message" + message);
    }
}
