package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.UserTestStatus;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTestStatusRepository extends Neo4jRepository<UserTestStatus, Long> {

    List<UserTestStatus> findByIdIn(List<Long> ids, @Depth int depth);

    //todo orderBy nem működik
    List<UserTestStatus> findByTestIdOrderByUserName(Long testId, @Depth int depth);

    //todo orderBy nem működik
    List<UserTestStatus> findByUserIdOrderByTestName(Long testId, @Depth int depth);

    Optional<UserTestStatus> findFirstByUserIdAndTestId(Long userId, Long testId, @Depth int depth);


}
