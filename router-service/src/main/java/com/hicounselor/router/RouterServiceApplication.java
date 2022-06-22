package com.hicounselor.router;

import com.hicounselor.matching.core.api.Job;
import com.hicounselor.router.producer.JobProducer;
import com.hicounselor.router.producer.MatchingJobProducer;

public class RouterServiceApplication {

    public static void main(String[] args) {
        System.out.println("Router service application");

        JobProducer<Job> jobProducer = new MatchingJobProducer();
        Job job = Job.builder()
                .id("1")
                .company("Google")
                .careerTrack("Software")
                .jobTitle("SDE1")
                .jobDescription("SDE1")
                .build();
        jobProducer.produce(job);
    }

}
