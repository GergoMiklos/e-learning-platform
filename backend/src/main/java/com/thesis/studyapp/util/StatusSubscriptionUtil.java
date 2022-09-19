package com.thesis.studyapp.util;

import com.thesis.studyapp.event.UpdatedStatusEvent;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.service.DefaultStudentStatusService;
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

    /**
     * Start subscription's thread and connect to it
     */
    @Autowired
    public StatusSubscriptionUtil(DefaultStudentStatusService studentStatusService) {
        this.studentStatusService = studentStatusService;
        updatedStatusIds = ConcurrentHashMap.newKeySet();
        executorService = Executors.newSingleThreadScheduledExecutor();

        Observable<StudentStatus> statusObservable = Observable.create(emitter -> {
            executorService.scheduleAtFixedRate(newSubscriptionTick(emitter), 0, BROADCAST_RATE_SECONDS, TimeUnit.SECONDS);
        });

        ConnectableObservable<StudentStatus> connectableObservable = statusObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    /**
     * Subscription for status changes by given test id
     */
    public Flowable<StudentStatus> getPublisherByTest(Long testId) {
        return publisher.filter(userTestStatus -> userTestStatus.getTest().getId().equals(testId));
    }

    /**
     * Subscription for status changes by given user id
     */
    public Flowable<StudentStatus> getPublisherByUser(Long userId) {
        return publisher.filter(userTestStatus -> userTestStatus.getUser().getId().equals(userId));
    }

    /**
     * Listening for status changes
     */
    @EventListener
    public void addNewUpdatedStatus(UpdatedStatusEvent updatedStatusEvent) {
        updatedStatusIds.add(updatedStatusEvent.getStudentStatusId());
    }

    /**
     * Subscription's thread tick
     * Load status changes and emit them
     */
    private Runnable newSubscriptionTick(ObservableEmitter<StudentStatus> emitter) {
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
        return studentStatusService.getStudentStatusesByIds(ids);
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
