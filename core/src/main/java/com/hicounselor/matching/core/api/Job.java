package com.hicounselor.matching.core.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    private String id;
    private String company;
    private String careerTrack;
    private String jobTitle;
    private String jobDescription;
    private boolean allocated;

}
