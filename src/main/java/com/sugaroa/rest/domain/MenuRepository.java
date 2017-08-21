package com.sugaroa.rest.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RepositoryRestResource(path = "menu")
public interface MenuRepository extends JpaRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {
    @RestResource(path = "text", rel = "text")
    Purview findByText(@Param("text") String text);

    List<Menu> findByStatusAndDeleted(@Param("status") int status, @Param("deleted") int deleted);

    //@RestResource(exported = false)
    //@Override
    //public void delete(Long id);

}
