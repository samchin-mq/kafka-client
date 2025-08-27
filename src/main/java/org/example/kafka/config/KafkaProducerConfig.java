package org.example.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.MicrometerProducerListener;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import io.micrometer.core.instrument.MeterRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafkaAddress}")
    String kafkaAddress;
    
    @Bean("producerFactory")
    @Primary
    public ProducerFactory<String, String> producerFactory(MeterRegistry meterRegistry) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-");
        configProps.put(ProducerConfig.METRIC_REPORTER_CLASSES_CONFIG, "org.apache.kafka.common.metrics.JmxReporter");
        configProps.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "INFO");
        // configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
//        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "tx-client-");
        // configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());
         configProps.put(ProducerConfig.LINGER_MS_CONFIG, "50");
        // configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        producerFactory.addListener(new MicrometerProducerListener<>(meterRegistry));
        return producerFactory;
    }

    @Bean("kafkaTemplate")
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
    
//    @Bean("kafkaTransactionManager")
//    @Primary
//    public KafkaTransactionManager<String, String> kafkaTransactionManager(ProducerFactory<String, String> producerFactory) {
//        return new KafkaTransactionManager<>(producerFactory);
//    }
    
    @Bean("stringProducerFactory")
    public ProducerFactory<String, String> stringProducerFactory(MeterRegistry meterRegistry) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, UUID.randomUUID()+"tx-");
        configProps.put(ProducerConfig.METRIC_REPORTER_CLASSES_CONFIG, "org.apache.kafka.common.metrics.JmxReporter");
        configProps.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "INFO");
        configProps.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, "-1");
        // configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
//        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "stx-client-");
        // configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());
        // configProps.put(ProducerConfig.LINGER_MS_CONFIG, "0");
        // configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        producerFactory.addListener(new MicrometerProducerListener<>(meterRegistry));
        return producerFactory;
    }

    @Bean("stringKafkaTemplate")
    public KafkaTemplate<String, String> stringKafkaTemplate(@Qualifier("stringProducerFactory") ProducerFactory<String, String> stringProducerFactory) {
        return new KafkaTemplate<>(stringProducerFactory);
    }
    
    @Bean("stringKafkaTransactionManager")
    public KafkaTransactionManager<String, String> stringKafkaTransactionManager(@Qualifier("stringProducerFactory") ProducerFactory<String, String> stringProducerFactory) {
        return new KafkaTransactionManager<>(stringProducerFactory);
    }
    
}