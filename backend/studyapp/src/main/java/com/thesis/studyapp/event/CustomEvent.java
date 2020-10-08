package com.thesis.studyapp.event;

import org.springframework.context.ApplicationEvent;

public class CustomEvent<T> extends ApplicationEvent {

    private final T data;

    public CustomEvent(Object source, T data) {
        super(source);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}

