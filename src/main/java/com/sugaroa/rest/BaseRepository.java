package com.sugaroa.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID> {
    List<Object[]> listBySQL(String sql);
}
