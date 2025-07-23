package org.example.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }
}


//kafka-topics --list --bootstrap-server <broker-url>:9098 --command-config client.properties
//kafka-topics.sh --list -bootstrap-server boot-9cf.exp2.5e7k1c.c4.kafka.ap-northeast-1.amazonaws.com:9092,boot-93g.exp2.5e7k1c.c4.kafka.ap-northeast-1.amazonaws.com:9092,boot-rzz.exp2.5e7k1c.c4.kafka.ap-northeast-1.amazonaws.com:9092 --command-config client.properties