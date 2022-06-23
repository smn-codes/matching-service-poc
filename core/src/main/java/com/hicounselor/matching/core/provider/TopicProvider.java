package com.hicounselor.matching.core.provider;

import java.util.HashMap;
import java.util.Map;

import static com.hicounselor.matching.core.Constants.DATA;
import static com.hicounselor.matching.core.Constants.SALESFORCE;
import static com.hicounselor.matching.core.Constants.SOFTWARE;

public class TopicProvider {

    private static final Map<String, String> TOPIC_MAP = new HashMap<>();

    static {
        TOPIC_MAP.put(SOFTWARE, "software-jobs");
        TOPIC_MAP.put(DATA, "data-jobs");
        TOPIC_MAP.put(SALESFORCE, "salesforce-jobs");
    }

    public static String getTopic(final String domain) {
        return TOPIC_MAP.get(domain);
    }

}
