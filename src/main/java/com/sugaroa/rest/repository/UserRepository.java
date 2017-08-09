package com.sugaroa.rest.repository;

import com.sugaroa.rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "user")
public interface UserRepository extends JpaRepository<User, Integer> {
    //List<User> findbyAccountLike(@Param("account") String account);
    //User findByAccount(@Param("account") String account);
    List<User[]> findByAccountLike(@Param("accountLike") String accountLike);
    User findByAccount(@Param("account") String account);
}
