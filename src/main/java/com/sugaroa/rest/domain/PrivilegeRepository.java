package com.sugaroa.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    List<Privilege> findByDeleted(@Param("deleted") int deleted);

    //查询获取部分字段
    @Query("select new Privilege(p.id,p.pid,p.text) from Privilege p where p.status=:status and p.deleted=:deleted")
    List<SimpleTree> findByStatusAndDeleted(@Param("status") int status, @Param("deleted") int deleted);

    List<Privilege> findByIdInAndStatusAndDeleted(Set<Integer> ids, int status, int deleted);

    Privilege findTopByResourceOrderByIdDesc(@Param("resource") String resource);

    int countByPidAndText(@Param("pid") int pid, @Param("text") String text);

    int countByPidAndTextAndIdNot(@Param("pid") int pid, @Param("text") String text, @Param("id") int id);
}
