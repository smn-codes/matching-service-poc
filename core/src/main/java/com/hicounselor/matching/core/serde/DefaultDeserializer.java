package com.hicounselor.matching.core.serde;

import com.hicounselor.matching.core.api.Job;
import com.hicounselor.matching.core.utils.JsonUtils;
import org.apache.kafka.common.serialization.Deserializer;

public class DefaultDeserializer implements Deserializer<Job> {

    @Override
    public Job deserialize(final String topic, final byte[] data) {
        return JsonUtils.readValue(data, Job.class);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }

}
