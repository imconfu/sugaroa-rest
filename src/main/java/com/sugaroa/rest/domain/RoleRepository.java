package com.sugaroa.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findByEnabledAndDeleted(@Param("enabled") int enabled, @Param("deleted") int deleted);

    List<Role> findByDeleted(@Param("deleted") Integer deleted);

    //查询获取部分字段
    @Query("select new Role(id,title) from Role where enabled=1 and deleted=0")
    List<Role> getSimpleList();
}
