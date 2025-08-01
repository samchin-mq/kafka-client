package org.example.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.kafka.dto.Order;
import org.example.kafka.dto.OrderShares;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    // Number of threads in the pool - adjust based on your system resources
    private static final int THREAD_POOL_SIZE = 200;
    BlockingQueue<Integer> stringTopicQueue = new ArrayBlockingQueue<>(THREAD_POOL_SIZE);
    BlockingQueue<Integer> stringTopic2Queue = new ArrayBlockingQueue<>(THREAD_POOL_SIZE);
    BlockingQueue<Integer> stringTopic3Queue = new ArrayBlockingQueue<>(THREAD_POOL_SIZE);
    Map<Integer, AtomicInteger> recordsPerCall = new HashMap<>();
    Map<String, Integer> threadRate = new ConcurrentHashMap<>();
    Map<String, Integer> producerRate = new ConcurrentHashMap<>();
    
    @PostConstruct
    void init() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            try {
                stringTopicQueue.put(i);
                stringTopic2Queue.put(i);
                stringTopic3Queue.put(i);
                recordsPerCall.put(i, new AtomicInteger(0));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            executor.submit(() -> sendMessage2("", ""));
        
        }
        
    }

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate kafkaTemplate;
    
    @Autowired
    @Qualifier("stringKafkaTemplate")
    private KafkaTemplate stringKafkaTemplate;
    
    @Autowired
    @Qualifier("serverlessKafkaTemplate")
    private KafkaTemplate serverlessKafkaTemplate;

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
            return null;
        });
    }
    
    @Transactional
    public void sendMessage() {
        Map<String, String> message = new HashMap<>();
        message.put("key", "message");
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send("topic",  message);
        }
    }
    
    @Transactional(transactionManager = "stringKafkaTransactionManager")
    public void sendStringMessage() {
        // try {
            // int partition = stringTopicQueue.poll(10, TimeUnit.SECONDS);
            for (int i = 0; i < 100; i++) {
                stringKafkaTemplate.send("stringtopic",  "abc");
            }
            // stringTopicQueue.put(partition);
        // } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        // }
    }
    
    @Transactional(transactionManager = "serverlessKafkaTransactionManager")
    public void sendStringMessage2() {
        for (int i = 0; i < 100; i++) {
            serverlessKafkaTemplate.send("stringtopic1", "abc");
        }
    }
    
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void sendStringMessage3() {
        // try {
            // int partition = stringTopic3Queue.poll(1, TimeUnit.SECONDS);
            threadRate.merge(Thread.currentThread().getName(), 1, Integer::sum);
            // recordsPerCall.get(partition).incrementAndGet();
            for (int i = 0; i < 100; i++) {
                // kafkaTemplate.send("stringtopicnew1",  partition, null ,"abc");
                kafkaTemplate.send("stringtopicnew2", "abc");
            }
            
            // stringTopic3Queue.put(partition);
        // } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        // }
    }
    
    @Transactional
    public void sendOrder(List<Order> orders) {
        log.info("sending order");
        orders.forEach( (order) -> {
            kafkaTemplate.send("orders", order);
        });
    }
    
    public Map<Integer, AtomicInteger> getRecords() {
        return recordsPerCall;
    }
    
    public Map<String, Integer> getThreadRate() {
        return threadRate;
    }
    
}
