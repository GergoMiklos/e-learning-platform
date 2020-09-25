package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.UserTestTaskStatus;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

@Repository
public interface UserTestTaskStatusRepository extends Neo4jRepository<UserTestTaskStatus, Long> {

    List<UserTestTaskStatus> findByIdIn(List<Long> ids, @Depth int depth);

    public static class UserTestTaskStatusComparator implements Comparator<UserTestTaskStatus> {

        @Override public int compare(UserTestTaskStatus utts1, UserTestTaskStatus utts2) {
            if (utts1.getLastSolutionTime().isAfter(utts2.getLastSolutionTime())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}