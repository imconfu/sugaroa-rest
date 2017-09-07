package com.sugaroa.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "menu")
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findByDeleted(@Param("deleted") int deleted);

//    //查询获取部分字段
//    @Query("select new Privilege(p.id,p.pid,p.text) from Menu p where p.status=:status and p.deleted=:deleted")
//    List<SimpleTree> findByStatusAndDeleted(@Param("status") int status, @Param("deleted") int deleted);

    //查询获取部分字段
    @Query("select new Menu(m.id,m.pid,m.text,m.href,m.privilegeArray) from Menu m where m.status=:status and m.deleted=:deleted")
    List<Menu> findByStatusAndDeleted(@Param("status") int status, @Param("deleted") int deleted);

    int countByPidAndText(@Param("pid") int pid, @Param("text") String text);

    int countByPidAndTextAndIdNot(@Param("pid") int pid, @Param("text") String text, @Param("id") int id);

    @RestResource(path = "text", rel = "text")
    Privilege findByText(@Param("text") String text);

    //List<Menu> findByStatusAndDeleted(@Param("status") int status, @Param("deleted") int deleted);

    //@RestResource(exported = false)
    //@Override
    //public void delete(Long id);

}
