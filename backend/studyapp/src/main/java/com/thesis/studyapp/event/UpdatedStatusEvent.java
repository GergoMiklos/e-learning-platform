package com.thesis.studyapp.event;

import org.springframework.context.ApplicationEvent;

public class UpdatedStatusEvent extends ApplicationEvent {

    private final Long studentStatusId;

    public UpdatedStatusEvent(Object source, Long studentStatusId) {
        super(source);
        this.studentStatusId = studentStatusId;
    }

    public Long getStudentStatusId() {
        return studentStatusId;
    }
}