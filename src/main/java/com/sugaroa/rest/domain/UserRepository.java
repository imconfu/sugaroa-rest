package com.sugaroa.rest.domain;

import com.sugaroa.rest.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.ColumnResult;
import java.util.List;

@RepositoryRestResource(path = "user")
//JpaRepository,PagingAndSortingRepository,SimpleJpaRepository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    //List<User> findbyAccountLike(@Param("account") String account);
    //User findByAccount(@Param("account") String account);
    List<User[]> findByAccountLike(@Param("accountLike") String accountLike);

    User findByAccount(@Param("account") String account);
}
