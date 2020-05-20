package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.NewsDTO;
import com.thesis.studyapp.model.News;
import com.thesis.studyapp.model.Task;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepo extends Neo4jRepository<News, Long> {
    @Query("MATCH (n:News) WHERE id(n) IN $0 " +
            "RETURN id(n) AS id, n.text AS text, n.creationDate AS creationDate")
    List<NewsDTO> getByManyIds(List<Long> Ids);

    @Query("MATCH (n:News) WHERE id(n) = $0 " +
            "RETURN id(n) AS id, n.text AS text, n.creationDate AS creationDate")
    Optional<NewsDTO> getById(Long Ids);

    @Query("MATCH (n:News)--(g:Group) WHERE id(g) = $0 " +
            "RETURN n")
    Optional<News> findByGroupId(Long groupIds);
}