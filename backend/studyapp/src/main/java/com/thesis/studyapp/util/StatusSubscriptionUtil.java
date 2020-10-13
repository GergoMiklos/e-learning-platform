package com.thesis.studyapp.util;

import com.thesis.studyapp.event.UpdatedStatusEvent;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.service.StudentStatusService;
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

    private final StudentStatusService studentStatusService;
    private final Logger logger = LoggerFactory.getLogger(StatusSubscriptionUtil.class);
    private final Set<Long> updatedStatusIds;
    private final Flowable<StudentStatus> publisher;
    private final ScheduledExecutorService executorService;
    private static final int BROADCAST_RATE_SECONDS = 5;


    @Override public void destroy() {
        executorService.shutdown();
    }

    @Autowired
    public StatusSubscriptionUtil(StudentStatusService studentStatusService) {
        this.studentStatusService = studentStatusService;
        updatedStatusIds = ConcurrentHashMap.newKeySet();
        executorService = Executors.newSingleThreadScheduledExecutor();

        Observable<StudentStatus> statusObservable = Observable.create(emitter -> {
            executorService.scheduleAtFixedRate(newUpdateTick(emitter), 0, BROADCAST_RATE_SECONDS, TimeUnit.SECONDS);
        });

        ConnectableObservable<StudentStatus> connectableObservable = statusObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<StudentStatus> getPublisher(Long testId) {
        return publisher.filter(userTestStatus -> userTestStatus.getTest().getId().equals(testId));
    }

    @EventListener
    public void addNewUpdatedStatus(UpdatedStatusEvent updatedStatusEvent) {
        updatedStatusIds.add(updatedStatusEvent.getData());
    }

    private Runnable newUpdateTick(ObservableEmitter<StudentStatus> emitter) {
        return () -> {
            List<StudentStatus> statuses = getUpdatedStatuses();
            if (statuses != null && !statuses.isEmpty()) {
                emitStatuses(emitter, statuses);
            }
        };
    }

    private List<StudentStatus> getUpdatedStatuses() {
        List<Long> ids = new ArrayList<>(updatedStatusIds);
        updatedStatusIds.clear();
        return studentStatusService.getUserTestStatusesByIds(ids);
    }

    private void emitStatuses(ObservableEmitter<StudentStatus> emitter, List<StudentStatus> statuses) {
        for (StudentStatus status : statuses) {
            try {
                emitter.onNext(status);
            } catch (RuntimeException e) {
                logger.error("Cannot publish subscription for updated StudentStatus", e);
            }
        }
    }

}
