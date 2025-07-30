package org.example.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.kafka.dto.Order;
import org.example.kafka.dto.OrderShares;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    // Number of threads in the pool - adjust based on your system resources
    private static final int THREAD_POOL_SIZE = 1_000;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public String sendMessagesInParallel(String topic, String message, int messageCount) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        try {
            // Create 1000 parallel tasks
            for (int i = 0; i < messageCount; i++) {
                final int messageNumber = i;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        String messageWithIndex = message + "-" + messageNumber;
                        sendMessage2(topic, messageWithIndex);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to send message " + messageNumber, e);
                    }
                }, executor);

                futures.add(future);
            }

            // Wait for all tasks to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);

            return "Successfully sent " + messageCount + " messages";
        } catch (Exception e) {
            return "Error sending messages: " + e.getMessage();
        } finally {
            // Shutdown the executor service
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    // Your existing sendMessage2 method
    public void sendMessage2(String topic, String message) {
        kafkaTemplate.executeInTransaction( template -> {
            kafkaTemplate.send(topic, message);
            return null;
        });
    }
    
    @Transactional
    public void sendMessage() {
        Map<String, String> message = new HashMap<>();
        message.put("key", "message");
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send("topic", message);
        }
    }
    
    @Transactional
    public void sendOrder(List<Order> orders) {
        log.info("sending order");
        orders.forEach( (order) -> {
            kafkaTemplate.send("orders", order);
        });
    }
    
    
}
