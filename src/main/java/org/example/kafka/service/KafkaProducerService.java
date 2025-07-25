package org.example.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    // Number of threads in the pool - adjust based on your system resources
    private static final int THREAD_POOL_SIZE = 1_000;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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
}
