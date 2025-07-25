package org.example.kafka.controller;

import org.example.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducerService producerService;

    @PostMapping("/publish")
    public String sendMessage(@RequestBody String message) {
        producerService.sendMessage2("sam-topic", message);
        return "Message sent successfully";
    }
    
    @PostMapping("/send-bulk")
    public String sendBulkMessages(@RequestParam String message) {
        return producerService.sendMessagesInParallel("sam-topic", message, 10_000);
    }
}
