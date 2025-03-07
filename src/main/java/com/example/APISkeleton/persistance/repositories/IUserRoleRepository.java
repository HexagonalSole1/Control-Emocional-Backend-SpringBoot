package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.pivots.UserRole;
import com.example.APISkeleton.persistance.entities.projections.IRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query(value = "SELECT r.id, r.name FROM users_roles AS ur " +
            "INNER JOIN users AS u ON u.id = ur.user_id " +
            "INNER JOIN roles AS r ON r.id = ur.role_id " +
            "WHERE ur.user_id = :userId", nativeQuery = true)
    List<IRoleProjection> getRolesByUserId(Long userId);
}