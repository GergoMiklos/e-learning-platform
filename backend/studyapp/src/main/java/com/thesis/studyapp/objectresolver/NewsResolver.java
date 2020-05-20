package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.*;
import com.thesis.studyapp.serviceresolver.LiveTestStateQuery;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class NewsResolver implements GraphQLResolver<NewsDTO> {

    public CompletableFuture<Long> sinceRefreshHours(NewsDTO news) {
        return CompletableFuture.supplyAsync(() -> {
            Date now = new Date();
            Date refreshTime = news.getCreationDate();
            if(refreshTime != null)
                return Duration.between(refreshTime.toInstant(), now.toInstant()).toHours();
            else
                return null;

        });
    }

}