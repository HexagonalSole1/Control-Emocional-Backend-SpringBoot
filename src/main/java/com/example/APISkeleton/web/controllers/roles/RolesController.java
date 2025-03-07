package com.example.APISkeleton.web.controllers.roles;

import com.example.APISkeleton.persistance.entities.Role;
import com.example.APISkeleton.persistance.entities.User;

import com.example.APISkeleton.persistance.entities.pivots.UserRole;
import com.example.APISkeleton.persistance.repositories.IRoleRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import com.example.APISkeleton.persistance.repositories.IUserRoleRepository;
import com.example.APISkeleton.web.dtos.responses.RoleResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RolesController {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IUserRoleRepository userRoleRepository;

    public RolesController(IRoleRepository roleRepository, IUserRepository userRepository, IUserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }


    @GetMapping
    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(role -> {
            RoleResponseDTO dto = new RoleResponseDTO();
            dto.setId(role.getId());
            dto.setName(role.getName());

            // 🔹 Obtener los emails de los usuarios asociados al rol
            dto.setUsers(role.getUserRoles()
                    .stream()
                    .map(userRole -> userRole.getUser().getEmail())
                    .collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }


    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Role newRole = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }


    @PostMapping("/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestParam Long userId, @RequestParam Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if (user.isEmpty() || role.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario o rol no encontrado");
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user.get());
        userRole.setRole(role.get());

        userRoleRepository.save(userRole);

        return ResponseEntity.ok("Rol asignado correctamente");
    }
}
