package com.yandex.market.userinfoservice.repository;

import com.yandex.market.userinfoservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN COUNT(u) = 1 THEN true ELSE false END FROM User u WHERE u.email=:email")
    boolean existsByEmail(String email);

    @Query("FROM User u WHERE u.externalId=:externalId AND u.isDeleted=false")
    Optional<User> findByExternalId(@Param("externalId") UUID externalId);

    @Modifying
    @Query("UPDATE User u SET isDeleted=true WHERE u.externalId=:externalId AND u.isDeleted=false")
    void deleteUserByExternalId(@Param("externalId") UUID externalId);

}
