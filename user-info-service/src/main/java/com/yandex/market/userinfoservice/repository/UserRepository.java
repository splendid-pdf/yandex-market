package com.yandex.market.userinfoservice.repository;

import com.yandex.market.userinfoservice.model.User;
import jakarta.persistence.criteria.From;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("FROM User u WHERE u.externalId=:externalId AND u.isDeleted=false")
    Optional<User> findByExternalId(@Param("externalId") UUID externalId);

    @Modifying
    @Query("UPDATE User u SET isDeleted = true WHERE u.externalId=:externalId AND u.isDeleted=false")
    void deleteUserByExternalId(@Param("externalId") UUID externalId);

    @Query("FROM User u WHERE u.email=:value OR u.phone=:value AND u.isDeleted=false")
    Optional<User> findUserByEmailOrPhone(String value);




}
