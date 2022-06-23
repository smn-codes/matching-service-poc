package com.hicounselor.matching.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hicounselor.matching.model.Candidate;

public class InMemoryCandidateRepository implements CandidateRepository {

    private final Map<String, Candidate> candidateMap = new HashMap<>();

    @Override
    public void put(final String id, final Candidate candidate) {
        candidateMap.put(id, candidate);
    }

    @Override
    public Candidate get(final String id) {
        return candidateMap.get(id);
    }

    @Override
    public Candidate getUnAssignedCandidate(final String domain) {
        Optional<Candidate> optionalCandidate = candidateMap.values().stream()
                .filter(candidate -> domain.equals(candidate.getCareerTrack()))
                .filter(candidate -> !candidate.isAllocated())
                .findFirst();
        return optionalCandidate.orElse(null);
    }

    @Override
    public List<Candidate> fetchAll() {
        return new ArrayList<>(candidateMap.values());
    }


}
