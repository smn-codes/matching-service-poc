package com.hicounselor.matching.repository;

import com.hicounselor.matching.core.api.Job;

public interface JobRepository {

    void put(String id, Job job);

    Job get(String id);

}
