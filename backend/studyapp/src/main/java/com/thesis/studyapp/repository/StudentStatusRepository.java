package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.StudentStatus;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentStatusRepository extends Neo4jRepository<StudentStatus, Long>, ObjectLoader<StudentStatus> {

    List<StudentStatus> findByIdIn(List<Long> ids, @Depth int depth);

    //todo orderBy nem működik
    List<StudentStatus> findByTestIdOrderByUserName(Long testId, @Depth int depth);

    //todo orderBy nem működik
    List<StudentStatus> findByUserIdOrderByTestName(Long testId, @Depth int depth);

    Optional<StudentStatus> findFirstByUserIdAndTestId(Long userId, Long testId, @Depth int depth);


}
