package com.hicounselor.router.producer;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.hicounselor.matching.core.api.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class MatchingJobProducer implements JobProducer<Job> {

    private KafkaProducer<String, Job> kafkaProducer;

    @Override
    public void produce(final Job job) {
        KafkaProducer<String, Job> kafkaProducer = getKafkaProducer();
        ProducerRecord<String, Job> producerRecord = new ProducerRecord<>("jobs", job.getCompany(), job);
        Future<RecordMetadata> metadataFuture = kafkaProducer.send(producerRecord);
        try {
            RecordMetadata recordMetadata = metadataFuture.get();
            log.debug("Produced a job to '{}' with offset '{}'", recordMetadata.topic(), recordMetadata.offset());
        }
        catch (InterruptedException | ExecutionException e) {
            log.error("Unable to produce the job '{}'", job.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void produce(final List<Job> jobs) {
        if (Objects.isNull(jobs)) {
            return;
        }
        for (final Job job : jobs) {
            produce(job);
        }
    }

    private KafkaProducer<String, Job> getKafkaProducer() {
        if (Objects.isNull(kafkaProducer)) {
            Properties properties = new Properties();
            properties.put("bootstrap.servers", "192.168.0.102:9092");
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "com.hicounselor.matching.core.serde.DefaultSerializer");
            kafkaProducer = new KafkaProducer<>(properties);
        }
        return kafkaProducer;
    }

    @Override
    public void close() throws Exception {
        if (Objects.isNull(kafkaProducer)) {
            kafkaProducer.close();
        }
    }
}
