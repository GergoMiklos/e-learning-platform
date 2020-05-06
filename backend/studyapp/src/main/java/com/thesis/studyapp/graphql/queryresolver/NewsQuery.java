package com.thesis.studyapp.graphql.queryresolver;

import com.thesis.studyapp.dao.NewsRepo;
import com.thesis.studyapp.dto.NewsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsQuery {
    @Autowired
    NewsRepo newsRepo;

    public List<NewsDTO> getByManyNewsIds(List<Long> newsIds) {
        return newsRepo.findByManyNewsIds(newsIds);
    }
}
