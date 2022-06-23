package com.hicounselor.matching.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;

import com.hicounselor.matching.cache.JobCache;
import com.hicounselor.matching.core.api.Job;
import com.hicounselor.matching.model.Candidate;
import com.hicounselor.matching.service.CandidateService;
import com.hicounselor.matching.service.JobService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import static com.hicounselor.matching.core.Constants.BOOTSTRAP_SERVERS_KEY;
import static com.hicounselor.matching.core.utils.JsonUtils.writeValueAsString;
import static com.hicounselor.matching.core.utils.PropertyUtils.getProperty;

@Slf4j
public class MatchingJobConsumer implements JobConsumer {

    private final String topic;
    private final String candidateId;
    private final String careeTrack;
    private final JobCache<String, Integer> jobCache;
    private final CandidateService candidateService;
    private final CandidatePickerService candidatePickerService;
    private final JobService jobService;

    private KafkaConsumer<String, Job> kafkaConsumer;
    private boolean signalStop = false;
    private boolean stopped = false;

    public MatchingJobConsumer(final String topic,
            final String careerTrack,
            final String candidateId,
            final JobCache<String, Integer> jobCache,
            final CandidateService candidateService,
            final CandidatePickerService candidatePickerService,
            final JobService jobService) {
        this.topic = topic;
        this.candidateId = candidateId;
        this.jobCache = jobCache;
        this.candidateService = candidateService;
        this.careeTrack = careerTrack;
        this.candidatePickerService = candidatePickerService;
        this.jobService = jobService;
        initialize();
    }

    private void initialize() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", getProperty(BOOTSTRAP_SERVERS_KEY, "localhost:9092"));
        String consumerGroup = "jobs-consumer-" + candidateId;
        props.setProperty("group.id", consumerGroup);
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "com.hicounselor.matching.core.serde.DefaultDeserializer");
        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }

    @SneakyThrows
    @Override
    public void consume() {
        while (true) {
            if (signalStop) {
                completeConsumption();
                break;
            }
            ConsumerRecords<String, Job> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, Job> consumerRecord : consumerRecords) {
                Job job = consumerRecord.value();
                if (job.getCareerTrack().equals(careeTrack)) {
                    Job existingJob = jobService.getJob(job.getId());
                    if (Objects.isNull(existingJob) || !existingJob.isAllocated()) {
                        System.out.println("Matching:  Offset - '" + consumerRecord.offset() + "' Company - '" + consumerRecord.key() + "' job - '" + writeValueAsString(job) + "'");
                        jobCache.put(candidateId, jobCache.get(candidateId) + 1);
                        Candidate candidate = candidateService.get(candidateId);
                        candidate.addJob(job.getId());
                        candidateService.save(candidate);
                        job.setAllocated(true);
                        jobService.save(job);
                    }
                }
                if (jobCache.get(candidateId) >= 10) {
                    stop();
                    break;
                }
            }
            kafkaConsumer.commitSync();
        }
    }

    private void completeConsumption() throws Exception {
        System.out.println("Stopping the consumer for the candidate " + candidateId);
        seekToEndOfTopic();
        System.out.println("Successfully stopped consumer for the candidate " + candidateId);
        updateCandidateWithJobAllocatedStatus();
        candidatePickerService.pickNextCandidate(careeTrack);
        log.info("Successfully stopped consumer for the candidate '{}'", candidateId);
        close();
        stopped = true;
    }

    private void seekToEndOfTopic() {
        kafkaConsumer.seekToEnd(kafkaConsumer.assignment());
        kafkaConsumer.poll(Duration.ofMillis(100));
    }

    private void updateCandidateWithJobAllocatedStatus() {
        Candidate candidate = candidateService.get(candidateId);
        candidate.setAllocated(true);
        candidateService.save(candidate);
    }

    @Override
    public void stop() {
        log.info("Stopping the consumer for the candidate '{}'", candidateId);
        signalStop = true;
    }

    @Override
    public String getCandidateId() {
        return candidateId;
    }

    @Override
    public void close() throws Exception {
        kafkaConsumer.close();
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

}
