package com.sugaroa.rest.repository;

import com.sugaroa.rest.entity.Purview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "purview")
public interface PurviewRepository extends JpaRepository<Purview, Integer> {
    @RestResource(path="text",rel="text")
    Purview findByText(@Param("text") String text);

    //@RestResource(exported = false)
    //@Override
    //public void delete(Long id);

}
