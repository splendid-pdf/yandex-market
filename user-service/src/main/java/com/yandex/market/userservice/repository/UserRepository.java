package com.yandex.market.userservice.repository;

import com.yandex.market.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT CASE WHEN COUNT(u) = 1 THEN true ELSE false END FROM User u WHERE u.email=:email")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) = 1 THEN true ELSE false END FROM User u WHERE u.phone=:phone")
    boolean existsByPhone(String phone);

    @Query("FROM User u LEFT JOIN FETCH u.contacts c" +
            " WHERE u.externalId=:externalId AND u.isDeleted=false")
    Optional<User> findByExternalId(@Param("externalId") UUID externalId);

    @Query("FROM User u WHERE u.email=:email AND u.isDeleted=false")
    Optional<User> findUserByEmail(String email);

    @Query("FROM User u WHERE u.phone=:phone AND u.isDeleted=false")
    Optional<User> findUserByPhone(String phone);
}