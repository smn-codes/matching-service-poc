package com.hicounselor.matching.consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ConsumerInfo {

    private final String topic;
    private final String candidateId;
    private final String domain;

}
