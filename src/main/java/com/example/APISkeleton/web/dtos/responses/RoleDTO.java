package com.example.APISkeleton.web.dtos.responses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {

    private String name;

    public RoleDTO( String name) {
        this.name = name;
    }
}
