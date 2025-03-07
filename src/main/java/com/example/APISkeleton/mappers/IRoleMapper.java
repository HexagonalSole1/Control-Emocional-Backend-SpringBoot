package com.example.APISkeleton.mappers;

import com.example.APISkeleton.persistance.entities.projections.IRoleProjection;
import com.example.APISkeleton.web.dtos.responses.GetRoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRoleMapper {
    GetRoleResponse toGetRoleResponse(IRoleProjection roleProjection);
}
