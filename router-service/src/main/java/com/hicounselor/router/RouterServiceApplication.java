package com.hicounselor.router;

import com.hicounselor.matching.core.Constants;
import com.hicounselor.matching.core.api.Job;
import com.hicounselor.router.producer.JobProducer;
import com.hicounselor.router.producer.MatchingJobProducer;

public class RouterServiceApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Router service application");

        JobProducer<Job> jobProducer = new MatchingJobProducer();

        for (int i = 101; i <= 110; i++) {
            Job job = Job.builder()
                    .id(i + "")
                    .company("Google")
                    .careerTrack(Constants.SALESFORCE)
                    .jobTitle("SDE1")
                    .jobDescription("SDE1")
                    .build();
            jobProducer.produce(job);
            Thread.sleep(20);
        }

        for (int i = 111; i <= 120; i++) {
            Job job = Job.builder()
                    .id(i + "")
                    .company("Google")
                    .careerTrack(Constants.SALESFORCE)
                    .jobTitle("SDE1")
                    .jobDescription("SDE1")
                    .build();
            jobProducer.produce(job);
            Thread.sleep(20);
        }

    }

}
