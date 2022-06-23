package com.hicounselor.matching.consumer;

public interface JobConsumer extends AutoCloseable {

    void consume();

    void stop();

    String getCandidateId();

    boolean isStopped();

}
