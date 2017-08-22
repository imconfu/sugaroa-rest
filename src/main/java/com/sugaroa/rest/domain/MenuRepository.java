package com.sugaroa.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "menu")
public interface MenuRepository extends JpaRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {
    @RestResource(path = "text", rel = "text")
    Authority findByText(@Param("text") String text);

    List<Menu> findByStatusAndDeleted(@Param("status") int status, @Param("deleted") int deleted);

    //@RestResource(exported = false)
    //@Override
    //public void delete(Long id);

}
