package com.example.APISkeleton.services.impls;

import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.entities.pivots.UserRole;
import com.example.APISkeleton.persistance.repositories.IRoleRepository;
import com.example.APISkeleton.services.IAuthService;
import com.example.APISkeleton.services.IUserService;
import com.example.APISkeleton.types.JWTType;
import com.example.APISkeleton.utils.IJWTUtils;
import com.example.APISkeleton.web.dtos.requests.AuthenticateRequest;
import com.example.APISkeleton.web.dtos.requests.RefreshTokenRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.dtos.responses.RoleDTO;
import com.example.APISkeleton.web.exceptions.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {
    private final IUserService userService;
    private final IJWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final IRoleRepository roleRepository;


    public AuthServiceImpl(IUserService userService, IJWTUtils jwtUtils, IRoleRepository roleRepository) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
    }

    @Override
    public BaseResponse authenticate(AuthenticateRequest request) {
        // 🔹 Verificar si el usuario existe
        User user = userService.getOptionalUserByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        // 🔹 Verificar si la contraseña es correcta
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // 🔹 Crear claims para el JWT (incluye ID y email)
        Map<String, Object> claims = Map.of(
                "id", user.getId(),
                "email", user.getEmail()
        );

        // 🔹 Generar tokens JWT
        String accessToken = jwtUtils.generateToken(user.getEmail(), claims, JWTType.ACCESS_TOKEN);
        String refreshToken = jwtUtils.generateToken(user.getEmail(), null, JWTType.REFRESH_TOKEN);
        Long idUser = user.getId();

        // 🔹 Convertir lista de UserRoles a RoleDTO y verificar que no sea null
        List<RoleDTO> roles = (user.getUserRoles() != null) ?
                user.getUserRoles().stream()
                        .map(userRole -> new RoleDTO(userRole.getRole().getName()))
                        .toList() : Collections.emptyList();

        // 🔹 Verificar valores antes de construir la respuesta
        String username = (user.getUsername() != null) ? user.getUsername() : "Unknown";
        String name = (user.getName() != null) ? user.getName() : "Unknown";

        // 🔹 Estructurar la respuesta en un Map con valores seguros
        Map<String, Object> tokens = Map.ofEntries(
                Map.entry("access_token", accessToken),
                Map.entry("refresh_token", refreshToken),
                Map.entry("id_user", idUser),
                Map.entry("username", username),
                Map.entry("name", name),
                Map.entry("roles", roles),
                Map.entry("fcm", user.getFCM())
        );

        // 🔹 Devolver respuesta exitosa
        return BaseResponse.builder()
                .data(tokens)
                .message("Successfully authenticated")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    @Override
    public BaseResponse refreshToken(RefreshTokenRequest request) {
        Boolean isTokenValid = jwtUtils.isTokenValid(request.getRefreshToken(), JWTType.REFRESH_TOKEN);

        if (!isTokenValid) {
            throw new InvalidCredentialsException();
        }

        String email = jwtUtils.getSubjectFromToken(request.getRefreshToken(), JWTType.REFRESH_TOKEN);
        Map<String, Object> claims = jwtUtils.getClaimsFromToken(request.getRefreshToken(), JWTType.REFRESH_TOKEN);

        String accessToken = jwtUtils.generateToken(email, claims, JWTType.ACCESS_TOKEN);
        String refreshToken = jwtUtils.generateToken(email, null, JWTType.REFRESH_TOKEN);

        Map<String, String> tokens = Map.of(
                "access_token", accessToken,
                "refresh_token", refreshToken
        );

        return BaseResponse.builder()
                .data(tokens)
                .message("Successfully refreshed token")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }
}
