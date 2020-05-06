package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.NewsDTO;
import com.thesis.studyapp.model.News;
import com.thesis.studyapp.model.Task;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepo extends Neo4jRepository<News, Long> {
    @Query("MATCH (n:News) WHERE id(n) IN $0 " +
            "RETURN id(n) AS id, n.title AS title, n.description AS  description")
    List<NewsDTO> findByManyNewsIds(List<Long> Ids);
}