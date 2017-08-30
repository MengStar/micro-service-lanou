package meng.xing.service;

import meng.xing.entity.UserRole;
import meng.xing.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserRole implements UserRoleService {
    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    @Cacheable(value = "AllRoles") //可以缓存
    public List<UserRole> findALlRoles() {
        return userRoleRepository.findAll();
    }
    @Override
    @Cacheable(value = "OneRole") //可以缓存
    public UserRole findUserRoleByRole(String role) {
        return userRoleRepository.findByRole(role);
    }
}
