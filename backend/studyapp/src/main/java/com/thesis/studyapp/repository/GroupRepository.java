package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.Group;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends Neo4jRepository<Group, Long> {
    //TODO RETURN_AS_....
    String RETURN_GROUPDTO = " RETURN id(g) AS id, g.name AS name, g.code AS code, g.description AS description, g.news AS news, g.newsChangedDate";
    String ORDER_BY_NAME = " ORDER BY g.name";

    Optional<Group> findByCode(String name, @Depth int depth);

    List<Group> findByIdIn(List<Long> groupIds, @Depth int depth);

    List<Group> findByStudentsIdOrderByName(Long studentId, @Depth int depth);

    List<Group> findByTeachersIdOrderByName(Long teacherId, @Depth int depth);

//    @Query("MATCH (g:Group) WHERE id(g) = $0 " + RETURN_GROUPDTO)
//    Optional<GroupDto> getById(Long groupId);
//
//    @Query("MATCH (g:Group) WHERE id(g) IN $0 " + RETURN_GROUPDTO)
//    List<GroupDto> getByIds(List<Long> groupIds);
//
//    @Query("MATCH (u:User)-[gs:GROUPSTUDENT]-(g:Group) WHERE id(u) = $0" + RETURN_GROUPDTO + ORDER_BY_NAME)
//    List<GroupDto> getByStudentId(Long userId);
//
//    @Query("MATCH (u:User)-[gt:GROUPTEACHER]-(g:Group) WHERE id(u) = $0" + RETURN_GROUPDTO + ORDER_BY_NAME)
//    List<GroupDto> getByTeacherId(Long userId);

    @Query("MATCH (u:User)-[gs:GROUPSTUDENT]-(g:Group)" +
            " WHERE id(u) = $0 AND id(g) = $1" +
            " DELETE gs")
    void deleteStudent(Long userId, Long groupId);

    @Query("MATCH (u:User)-[gt:GROUPTEACHER]-(g:Group)" +
            " WHERE id(u) = $0 AND id(g) = $1" +
            " DELETE gt")
    void deleteTeacher(Long groupId, Long userId);
}
