package com.hicounselor.matching.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Candidate {

    private String id;
    private String name;
    private String careerTrack;
    private boolean allocated;
    private final List<String> jobs = new ArrayList<>();

    public void addJob(String jobId) {
        jobs.add(jobId);
    }

}
