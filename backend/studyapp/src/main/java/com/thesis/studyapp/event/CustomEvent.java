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


//todo egy event a testservice-nek, aztán az visszatér egy evenettel  a subscriptnak?
//@EqualsAndHashCode(callSuper = true)
//@Builder
//@Data
//public class CustomEvent extends ApplicationEvent {
//    private Long testId;
//
//    public CustomEvent(Object source, Long testId) {
//        super(source);
//        this.testId = testId;
//    }
//}
