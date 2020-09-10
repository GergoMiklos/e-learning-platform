package com.thesis.studyapp.repository;

import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.model.UserTestStatus;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTestStatusRepository extends Neo4jRepository<UserTestStatus, Long> {
    String RETURN_USERTESTSTATUSDTO =
            " WITH uts," +
                    " head([(uts:UserTestStatus)--(u:User) | id(u)]) AS userId," +
                    " head([(uts:UserTestStatus)--(:LiveTest)--(t:Test) | id(t)]) AS testId," +
                    " head([(uts:UserTestStatus)--(ct:Task) | id(ct)]) AS currentTaskId" +
                    " RETURN DISTINCT id(lts) AS id, userId, testId, currentTaskId";

    @Query("MATCH (uts:UserTestStatus)--(t:Test) WHERE uts.deprecated = false AND id(t) = $0" + RETURN_USERTESTSTATUSDTO)
    List<UserTestStatusDto> getByTestId(Long testId);

    @Query("MATCH (uts:UserTestStatus) WHERE uts.deprecated = false AND id(uts) IN $0" + RETURN_USERTESTSTATUSDTO)
    List<UserTestStatusDto> getByManyIds(List<Long> ids);

}
