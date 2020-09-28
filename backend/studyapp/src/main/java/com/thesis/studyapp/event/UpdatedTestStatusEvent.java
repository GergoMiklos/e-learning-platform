package com.thesis.studyapp.event;

public class UpdatedTestStatusEvent extends CustomEvent<Long> {
    public UpdatedTestStatusEvent(Object source, Long testId) {
        super(source, testId);
    }
}
