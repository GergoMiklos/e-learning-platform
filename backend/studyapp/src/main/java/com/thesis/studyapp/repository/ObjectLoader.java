package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.HasId;

import java.util.List;

public interface ObjectLoader<T extends HasId> {

    List<T> findByIdIn(List<Long> ids, int depth);
}
