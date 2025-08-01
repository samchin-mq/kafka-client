package org.example.kafka.controller;


import org.example.kafka.dto.Order;
import org.example.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicLong;
import java.time.Duration;
import java.time.Instant;

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
    
    @PostMapping("/send-bulk")
    public String sendBulkMessages(@RequestParam String message) {
        return producerService.sendMessagesInParallel("sam-topic", message, 10_000);
    }
    
    @GetMapping("/send")
    public void send() {
        producerService.sendMessage();
    }
    
    @GetMapping("/sendString")
    public void sendString() {
        producerService.sendStringMessage();
    }
    
    @PostMapping("/sendOrder")
    public void sendOrder(@RequestBody List<Order> orders) {
        producerService.sendOrder(orders);
    }
    
    @GetMapping("/sendString2")
    public void sendString2() {
        producerService.sendStringMessage2();
    }
    
    @GetMapping("/sendString3")
    public void sendString3() {
        count.incrementAndGet();
        Instant start = Instant.now();
        producerService.sendStringMessage3();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        executionTotal.accumulateAndGet(duration.toMillis(), Long::sum);
    }
    
    @GetMapping("/stat")
    public void stat() {
        System.out.println(producerService.getRecords());
        System.out.println(producerService.getThreadRate());
        System.out.println(count.get());
        System.out.println(executionTotal.get());
    }
}
