package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users AS u WHERE u.email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users AS u WHERE u.fcm = :FCM", nativeQuery = true)
    Optional<User> findByFCM(String FCM);

}
