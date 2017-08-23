package com.sugaroa.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    List<Privilege> findByDeleted(@Param("deleted") int deleted);
}
