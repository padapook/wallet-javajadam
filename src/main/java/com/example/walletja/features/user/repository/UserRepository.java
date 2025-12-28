package com.example.walletja.features.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.walletja.features.user.entity.UserEntity;

import java.util.List;       
import java.util.Optional;                      

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.isDeleted = false")
    Optional<UserEntity> findActiveUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    Optional<UserEntity> findUserByUsername(@Param("username") String username);

    List<UserEntity> findAllByIsDeletedFalse();
    
}