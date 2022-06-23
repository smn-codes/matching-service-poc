package com.hicounselor.matching.service;

import com.hicounselor.matching.core.api.Job;
import com.hicounselor.matching.repository.JobRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public synchronized void save(final Job job) {
        jobRepository.put(job.getId(), job);
    }

    public Job getJob(final String id) {
        return jobRepository.get(id);
    }

}
