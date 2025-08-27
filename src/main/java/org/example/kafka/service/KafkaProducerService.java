package org.example.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    Map<Integer, AtomicInteger> recordsPerCall = new HashMap<>();
    Map<String, Integer> threadRate = new ConcurrentHashMap<>();

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate kafkaTemplate;
    
    @Autowired
    @Qualifier("stringKafkaTemplate")
    private KafkaTemplate stringKafkaTemplate;

    // Your existing sendMessage2 method
    public void sendMessage2(String topic, String message) {
        kafkaTemplate.executeInTransaction( template -> {
            return null;
        });
    }
    
//    @Transactional
    public void sendMessage() {
        String data = """
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                """;
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send("topic",  data);
        }
    }
    
    @Transactional(transactionManager = "stringKafkaTransactionManager")
    public void sendStringMessage() {
        String data = """
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                abcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyzabcdefghijklmnoprstuvwxyz
                """;
        // try {
            // int partition = stringTopicQueue.poll(10, TimeUnit.SECONDS);
            for (int i = 0; i < 100; i++) {
                stringKafkaTemplate.send("stringtopic",  data);
            }
            // stringTopicQueue.put(partition);
        // } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        // }
    }
    
    public Map<Integer, AtomicInteger> getRecords() {
        return recordsPerCall;
    }
    
    public Map<String, Integer> getThreadRate() {
        return threadRate;
    }
    
}
