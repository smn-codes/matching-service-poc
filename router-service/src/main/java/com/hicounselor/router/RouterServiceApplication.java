package com.hicounselor.router;

import com.hicounselor.matching.core.Constants;
import com.hicounselor.matching.core.api.Job;
import com.hicounselor.router.producer.JobProducer;
import com.hicounselor.router.producer.MatchingJobProducer;

public class RouterServiceApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Router service application");

        JobProducer<Job> jobProducer = new MatchingJobProducer();

        for (int i = 38; i < 43; i++) {
            Job job = Job.builder()
                    .id(i + "")
                    .company("Google")
                    .careerTrack(Constants.DATA)
                    .jobTitle("SDE1")
                    .jobDescription("SDE1")
                    .build();
            jobProducer.produce(job);
            Thread.sleep(20);
        }

    }

}
