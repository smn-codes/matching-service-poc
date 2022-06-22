package com.hicounselor.router.producer;

import java.util.List;

import com.hicounselor.matching.core.api.Job;

public interface JobProducer<T extends Job> extends AutoCloseable {

    void produce(final T job);

    void produce(final List<T> job);

}
