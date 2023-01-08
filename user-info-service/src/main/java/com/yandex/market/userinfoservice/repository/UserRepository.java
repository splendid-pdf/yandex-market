package com.yandex.market.userinfoservice.repository;

import com.yandex.market.userinfoservice.model.User;
import jakarta.persistence.criteria.From;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN COUNT(u) = 1 THEN true ELSE false END FROM User u WHERE u.email=:email")
    boolean existsByEmail(String email);

    @Query("FROM User u WHERE u.externalId=:externalId AND u.isDeleted=false")
    Optional<User> findByExternalId(@Param("externalId") UUID externalId);

    @Modifying
    @Query("UPDATE User u SET isDeleted = true WHERE u.externalId=:externalId AND u.isDeleted=false")
    void deleteUserByExternalId(@Param("externalId") UUID externalId);

//    @Query("FROM User u WHERE u.email=:value OR u.phone=:value AND u.isDeleted=false")
//    Optional<User> findUserByEmailOrPhone(String value);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPhone(String phone);

}
