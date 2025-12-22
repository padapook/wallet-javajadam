package com.example.walletja.repository;

import com.example.walletja.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT * FROM User WHERE isDeleted = false")
    List<User> findAllActiveUsers();
}