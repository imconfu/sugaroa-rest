package com.sugaroa.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "authority")
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    List<Authority> findByDeleted( @Param("deleted") int deleted);
}
