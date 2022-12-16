package com.market.userinfoservice.repository;

import com.market.userinfoservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User u WHERE u.externalId=:externalId AND u.isDeleted=false")
    Optional<User> findByExternalId(@Param("externalId") UUID externalId);
    @Modifying
    @Query("UPDATE User u SET isDeleted=true WHERE u.externalId=:externalId AND u.isDeleted=false")
    void deleteUserByExternalId (@Param("externalId") UUID externalId);
}
