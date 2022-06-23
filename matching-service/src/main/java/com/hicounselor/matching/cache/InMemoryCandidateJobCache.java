package com.hicounselor.matching.cache;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCandidateJobCache implements JobCache<String, Integer> {

    public final Map<String, Integer> candidateJobCount = new HashMap<>();

    @Override
    public Integer get(final String key) {
        return candidateJobCount.getOrDefault(key, 0);
    }

    @Override
    public void put(final String key, final Integer value) {
        candidateJobCount.put(key, value);
    }

}
