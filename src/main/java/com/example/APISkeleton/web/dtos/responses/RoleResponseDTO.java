package com.example.APISkeleton.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RoleResponseDTO {
    private Long id;
    private String name;
    private List<String> users; // 🔹 Solo enviamos los emails de los usuarios
}
