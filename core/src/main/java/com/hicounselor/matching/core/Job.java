package com.hicounselor.matching.core;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Job {

    private String id;
    private String company;
    private String careerTrack;
    private String jobTitle;
    private String jobDescription;

}
