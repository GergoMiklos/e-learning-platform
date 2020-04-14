package com.thesis.studyapp.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
interface BaseRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findByUuid(String uuid);

    <S extends T> S save(S entity);
}
