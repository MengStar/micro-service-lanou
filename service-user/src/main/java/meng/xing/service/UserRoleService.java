package meng.xing.service;

import meng.xing.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findALlRoles();
    UserRole findUserRoleByRole(String role);
}
