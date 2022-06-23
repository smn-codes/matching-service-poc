package com.hicounselor.matching.repository;

import com.hicounselor.matching.model.Candidate;

public interface CandidateRepository {

    void put(String id, Candidate candidate);

    Candidate get(String id);

    Candidate getUnAssignedCandidate(String domain);

}
