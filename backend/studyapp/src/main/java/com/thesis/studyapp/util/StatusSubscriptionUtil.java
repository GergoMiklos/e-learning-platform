package com.thesis.studyapp.util;

import com.thesis.studyapp.event.UpdatedStatusEvent;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.service.UserTestStatusService;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StatusSubscriptionUtil implements DisposableBean {

    private final UserTestStatusService userTestStatusService;
    private final Logger logger = LoggerFactory.getLogger(StatusSubscriptionUtil.class);
    private final Set<Long> updatedStatusIds;
    private final Flowable<UserTestStatus> publisher;
    private final ScheduledExecutorService executorService;
    private static final int BROADCAST_RATE_SECONDS = 5;


    @Override public void destroy() {
        executorService.shutdown();
    }

    @Autowired
    public StatusSubscriptionUtil(UserTestStatusService userTestStatusService) {
        this.userTestStatusService = userTestStatusService;
        updatedStatusIds = ConcurrentHashMap.newKeySet();
        executorService = Executors.newSingleThreadScheduledExecutor();

        Observable<UserTestStatus> statusObservable = Observable.create(emitter -> {
            executorService.scheduleAtFixedRate(newUpdateTick(emitter), 0, BROADCAST_RATE_SECONDS, TimeUnit.SECONDS);
        });

        ConnectableObservable<UserTestStatus> connectableObservable = statusObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<UserTestStatus> getPublisher(Long testId) {
        return publisher.filter(userTestStatus -> userTestStatus.getTest().getId().equals(testId));
    }

    @EventListener
    public void addNewUpdatedStatus(UpdatedStatusEvent updatedStatusEvent) {
        updatedStatusIds.add(updatedStatusEvent.getData());
    }

    private Runnable newUpdateTick(ObservableEmitter<UserTestStatus> emitter) {
        return () -> {
            List<UserTestStatus> statuses = getUpdatedStatuses();
            if (statuses != null && !statuses.isEmpty()) {
                emitTests(emitter, statuses);
            }
        };
    }

    private List<UserTestStatus> getUpdatedStatuses() {
        List<Long> ids = new ArrayList<>(updatedStatusIds);
        updatedStatusIds.clear();
        return userTestStatusService.getUserTestStatusesByIds(ids);
    }

    private void emitTests(ObservableEmitter<UserTestStatus> emitter, List<UserTestStatus> statuses) {
        for (UserTestStatus status : statuses) {
            try {
                emitter.onNext(status);
            } catch (RuntimeException e) {
                logger.error("Cannot publish subscription for updated UserTestStatus", e);
            }
        }
    }

}
