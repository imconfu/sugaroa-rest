package com.sugaroa.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "menu")
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    public List<Permission> findByEnabledAndDeleted(int enabled, int deleted);

    int countByParentIdAndText(@Param("parentId") int parentId, @Param("text") String text);

    int countByParentIdAndTextAndIdNot(@Param("parentId") int parentId, @Param("text") String text, @Param("id") int id);

    List<Permission> findByDeleted(@Param("deleted") Integer deleted);
}
