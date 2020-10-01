package com.thesis.studyapp.event;

public class UpdatedStatusEvent extends CustomEvent<Long> {
    public UpdatedStatusEvent(Object source, Long userTestStatusId) {
        super(source, userTestStatusId);
    }
}
