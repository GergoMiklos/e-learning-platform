package com.thesis.studyapp.util;

import com.thesis.studyapp.event.UpdatedTestStatusEvent;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.service.TestService;
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
public class TestSubscriptionUtil implements DisposableBean, SubscriptionUtil<Long, Test> {

    private final TestService testService;
    private final Logger logger = LoggerFactory.getLogger(TestSubscriptionUtil.class);
    private final Set<Long> updatedTestIds;
    private final Flowable<Test> publisher;
    private final ScheduledExecutorService executorService;
    private static final int BROADCAST_RATE_SECONDS = 15;


    @Override public void destroy() {
        executorService.shutdown();
    }

    @Autowired
    public TestSubscriptionUtil(TestService testService) {
        this.testService = testService;
        updatedTestIds = ConcurrentHashMap.newKeySet();
        executorService = Executors.newSingleThreadScheduledExecutor();

        Observable<Test> testObservable = Observable.create(emitter -> {
            executorService.scheduleAtFixedRate(newUpdateTick(emitter), 0, BROADCAST_RATE_SECONDS, TimeUnit.SECONDS);
        });

        ConnectableObservable<Test> connectableObservable = testObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Test> getPublisher(Long testId) {
        return publisher.filter(test -> test.getId().equals(testId));
    }

    @EventListener
    public void addNewUpdatedTest(UpdatedTestStatusEvent updatedTestStatusEvent) {
        updatedTestIds.add(updatedTestStatusEvent.getData());
    }

    private Runnable newUpdateTick(ObservableEmitter<Test> emitter) {
        return () -> {
            List<Test> tests = getUpdatedTests();
            if (tests != null && !tests.isEmpty()) {
                emitTests(emitter, tests);
            }
        };
    }

    private List<Test> getUpdatedTests() {
        List<Long> testIds = new ArrayList<>(updatedTestIds);
        updatedTestIds.clear();
        return testService.getTestsByIds(testIds);
    }

    private void emitTests(ObservableEmitter<Test> emitter, List<Test> tests) {
        for (Test test : tests) {
            try {
                emitter.onNext(test);
            } catch (RuntimeException e) {
                logger.error("Cannot publish subscription for updated Test", e);
            }
        }
    }

}
