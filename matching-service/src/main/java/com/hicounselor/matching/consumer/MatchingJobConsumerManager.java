package com.hicounselor.matching.consumer;

import java.util.HashMap;
import java.util.Map;

import com.hicounselor.matching.cache.JobCache;
import com.hicounselor.matching.core.provider.TopicProvider;
import com.hicounselor.matching.service.CandidateService;
import com.hicounselor.matching.service.JobService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchingJobConsumerManager {

    private final JobCache<String, Integer> jobCache;
    private final CandidateService candidateService;
    private final CandidatePickerService candidatePickerService;
    private final JobService jobService;
    private final Map<String, JobConsumer> kafkaConsumerMap = new HashMap<>();

    public void runConsumer(final String domain, final String candidateId) throws Exception {
        if (kafkaConsumerMap.containsKey(domain)) {
            JobConsumer jobConsumer = kafkaConsumerMap.get(domain);
            if (candidateId.equals(jobConsumer.getCandidateId())) {
                System.out.println("Consumer is already running for the '" + domain + "' and the candidate '" + candidateId + "'");
            }
            else {
                jobConsumer.stop();
                while (!jobConsumer.isStopped()) {
                    System.out.println("Checking consumer stopped or not.");
                    Thread.sleep(100);
                }
                System.out.println("Consumer for '" + domain + "' is stopped for the candidate '" + candidateId + "'");
                JobConsumer newJobConsumer = createJobConsumer(domain, candidateId);
                new Thread(newJobConsumer::consume).start();
                System.out.println("Started consumer for the '" + domain + "' and the candidate '" + candidateId + "'");
                kafkaConsumerMap.put(domain, newJobConsumer);
            }
        }
        else {
            JobConsumer newJobConsumer = createJobConsumer(domain, candidateId);
            new Thread(newJobConsumer::consume).start();
            kafkaConsumerMap.put(domain, newJobConsumer);
            System.out.println("Started consumer for the '" + domain + "' and the candidate '" + candidateId + "'");
        }
    }

    private JobConsumer createJobConsumer(final String domain, final String candidateId) {
        System.out.println("Creating new consumer for the '" + domain + "' for the candidate '" + candidateId + "'");
        return new MatchingJobConsumer(TopicProvider.getTopic(domain), domain, candidateId, jobCache, candidateService, candidatePickerService, jobService);
    }

}
