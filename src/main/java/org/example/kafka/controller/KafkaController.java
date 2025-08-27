package org.example.kafka.controller;


import org.example.kafka.dto.Order;
import org.example.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducerService producerService;
    private final AtomicLong count = new AtomicLong(0);
    private final AtomicLong executionTotal = new AtomicLong(0);

    @PostMapping("/publish")
    public String sendMessage(@RequestBody String message) {
        producerService.sendMessage2("sam-topic", message);
        return "Message sent successfully";
    }

    
    @GetMapping("/send")
    public void send() {
        producerService.sendMessage();
    }
    
    @GetMapping("/sendString")
    public void sendString() {
        producerService.sendStringMessage();
    }
    
    @GetMapping("/stat")
    public void stat() {
        System.out.println(producerService.getRecords());
        System.out.println(producerService.getThreadRate());
        System.out.println(count.get());
        System.out.println(executionTotal.get());
    }
}
