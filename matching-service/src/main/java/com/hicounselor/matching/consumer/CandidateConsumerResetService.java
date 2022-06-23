package com.hicounselor.matching.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import static com.hicounselor.matching.core.Constants.BOOTSTRAP_SERVERS_KEY;
import static com.hicounselor.matching.core.Constants.CONTROL_EVENT_TOPIC;
import static com.hicounselor.matching.core.utils.PropertyUtils.getProperty;

public class CandidateConsumerResetService implements Runnable {

    private final MatchingJobConsumerManager matchingJobConsumerManager;
    private KafkaConsumer<String, String> kafkaConsumer;
    private boolean signalStop = false;

    public CandidateConsumerResetService(final MatchingJobConsumerManager matchingJobConsumerManager) {
        this.matchingJobConsumerManager = matchingJobConsumerManager;
        initialize();
    }

    private void initialize() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", getProperty(BOOTSTRAP_SERVERS_KEY, "localhost:9092"));

        props.setProperty("group.id", CONTROL_EVENT_TOPIC + "-" + UUID.randomUUID());
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Collections.singletonList(CONTROL_EVENT_TOPIC));
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!signalStop) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(5));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                String candidateId = consumerRecord.value();
                if (!"NO_CANDIDATE".equals(candidateId)) {
                    matchingJobConsumerManager.runConsumer(consumerRecord.key(), candidateId);
                    signalStop = true;
                    break;
                }
            }
        }
    }

}
