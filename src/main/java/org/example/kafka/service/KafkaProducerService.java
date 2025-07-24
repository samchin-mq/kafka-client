package org.example.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
    
    public void sendMessage2(String topic, String message) {
        kafkaTemplate.executeInTransaction( template -> {
            kafkaTemplate.send(topic, message);
            return null;
        });
    }
}
