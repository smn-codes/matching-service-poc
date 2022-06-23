package com.hicounselor.matching.service;

import com.hicounselor.matching.model.Candidate;
import com.hicounselor.matching.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public Candidate get(final String id) {
        return candidateRepository.get(id);
    }

    public void save(final Candidate candidate) {
        candidateRepository.put(candidate.getId(), candidate);
    }

    public Candidate getNextAvailableCandidateByDomain(final String domain) {
        return candidateRepository.getUnAssignedCandidate(domain);
    }

}
