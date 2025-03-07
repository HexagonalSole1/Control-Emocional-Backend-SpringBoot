package com.example.APISkeleton.services.impls;

import com.example.APISkeleton.mappers.IRoleMapper;
import com.example.APISkeleton.persistance.repositories.IUserRoleRepository;
import com.example.APISkeleton.services.IUserRoleService;
import com.example.APISkeleton.web.dtos.responses.GetRoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements IUserRoleService {
    private final IUserRoleRepository repository;
    private final IRoleMapper roleMapper;

    public UserRoleServiceImpl(IUserRoleRepository userRoleRepository, IRoleMapper roleMapper) {
        this.repository = userRoleRepository;
        this.roleMapper = roleMapper;
    }

    public List<GetRoleResponse> getRolesByUserId(Long userId) {

        return repository.getRolesByUserId(userId)
                .stream()
                .map(roleMapper::toGetRoleResponse)
                .collect(Collectors.toList());
    }
}
