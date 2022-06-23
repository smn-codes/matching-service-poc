package com.hicounselor.matching.repository;

import java.util.HashMap;
import java.util.Map;

import com.hicounselor.matching.core.api.Job;

public class InMemoryJobRepository implements JobRepository {

    private Map<String, Job> jobMap = new HashMap<>();

    @Override
    public void put(final String id, final Job job) {
        jobMap.put(id, job);
    }

    @Override
    public Job get(final String id) {
        return jobMap.get(id);
    }
    
}
