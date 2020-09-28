package com.thesis.studyapp.util;

import io.reactivex.Flowable;

public interface SubscriptionUtil<S, T> {

    Flowable<T> getPublisher(S filter);

}
