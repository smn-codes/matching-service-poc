package com.hicounselor.matching;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hicounselor.matching.cache.InMemoryCandidateJobCache;
import com.hicounselor.matching.cache.JobCache;
import com.hicounselor.matching.consumer.CandidateConsumerResetService;
import com.hicounselor.matching.consumer.CandidatePickerService;
import com.hicounselor.matching.consumer.MatchingJobConsumerManager;
import com.hicounselor.matching.model.Candidate;
import com.hicounselor.matching.repository.InMemoryCandidateRepository;
import com.hicounselor.matching.repository.InMemoryJobRepository;
import com.hicounselor.matching.service.CandidateService;
import com.hicounselor.matching.service.JobService;

import static com.hicounselor.matching.core.Constants.DATA;
import static com.hicounselor.matching.core.Constants.SALESFORCE;
import static com.hicounselor.matching.core.Constants.SOFTWARE;

public class MatchingServiceApplication {

    public static void main(String[] args) throws Exception {

        System.out.println("Matching Service Application");

        JobCache<String, Integer> jobCache = new InMemoryCandidateJobCache();
        JobService jobService = new JobService(new InMemoryJobRepository());
        CandidateService candidateService = new CandidateService(new InMemoryCandidateRepository());
        CandidatePickerService candidatePickerService = new CandidatePickerService(candidateService);

        candidateService.save(Candidate.builder().id("C1").name("Customer C1").careerTrack(SOFTWARE).build());
        candidateService.save(Candidate.builder().id("C2").name("Customer C2").careerTrack(SOFTWARE).build());
        candidateService.save(Candidate.builder().id("C3").name("Customer C3").careerTrack(DATA).build());
        candidateService.save(Candidate.builder().id("C4").name("Customer C4").careerTrack(DATA).build());
        candidateService.save(Candidate.builder().id("C5").name("Customer C5").careerTrack(SALESFORCE).build());
        candidateService.save(Candidate.builder().id("C6").name("Customer C6").careerTrack(SALESFORCE).build());

        MatchingJobConsumerManager consumerManager = new MatchingJobConsumerManager(jobCache, candidateService, candidatePickerService, jobService);
        startConsumer(SOFTWARE, candidateService, consumerManager);
        startConsumer(DATA, candidateService, consumerManager);
        startConsumer(SALESFORCE, candidateService, consumerManager);

        CandidateConsumerResetService resetService = new CandidateConsumerResetService(consumerManager);
        new Thread(resetService).start();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<Candidate> candidates = candidateService.fetchAll();
            candidates.forEach(candidate -> System.out.println(candidate.getId() + " ::: " + candidate.getJobs()));
        }, 1, 10, TimeUnit.SECONDS);

    }

    private static void startConsumer(final String domain,
            final CandidateService candidateService, final MatchingJobConsumerManager consumerManager) throws Exception {

        Candidate candidate = candidateService.getNextAvailableCandidateByDomain(domain);
        if (Objects.nonNull(candidate)) {
            consumerManager.runConsumer(domain, candidate.getId());
        }
        else {
            System.out.println("No candidate available to allocate jobs");
        }
    }

}
