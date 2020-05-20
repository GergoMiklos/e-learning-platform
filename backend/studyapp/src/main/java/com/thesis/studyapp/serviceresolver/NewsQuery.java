package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dao.NewsRepo;
import com.thesis.studyapp.dto.NewsDTO;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class NewsQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    NewsRepo newsRepo;

    @Autowired
    GroupRepo groupRepo;



    public List<NewsDTO> getByManyNewsIds(List<Long> newsIds) {
        return newsRepo.getByManyIds(newsIds);
    }

    //TODO OPTINAL KEZELÉS MMINDENHOL !!!!
    @Transactional
    public Optional<NewsDTO> changeNews(Long groupId, String text) {
        News news;
        Optional<News> original = newsRepo.findByGroupId(groupId);
        if(original.isPresent()) {
            news = original.get();
        } else {
            news = new News();
            Group group = groupRepo.findById(groupId, 0).orElseThrow(()-> new CustomGraphQLException("No group with id: " + groupId));
            news.setGroup(group);
        }
        news.setText(text);
        news.setCreationDate(new Date());
        newsRepo.save(news);
        News created = newsRepo.save(news);
        return newsRepo.getById(created.getId());
    }
}
