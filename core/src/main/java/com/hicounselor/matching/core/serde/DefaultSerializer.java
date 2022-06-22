package com.hicounselor.matching.core.serde;

import com.hicounselor.matching.core.api.Job;
import com.hicounselor.matching.core.utils.JsonUtils;
import org.apache.kafka.common.serialization.Serializer;

public class DefaultSerializer implements Serializer<Job> {

    @Override
    public byte[] serialize(final String topic, final Job data) {
        try {
            return JsonUtils.writeValueAsBytes(data);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }

}
