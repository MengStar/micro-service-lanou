package meng.xing.service;

import meng.xing.entity.User;
import meng.xing.entity.UserRole;
import meng.xing.repository.UserRepository;
import meng.xing.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户管理Service
 * 部分方法可缓存
 */
@Service
public class DefaultUser implements UserService {
    private final Logger logger = LoggerFactory.getLogger(DefaultUser.class);
    final UserRepository userRepository;
    final UserRoleService userRoleService;

    @Autowired
    public DefaultUser(UserRoleService userRoleService, UserRepository userRepository) {
        this.userRoleService = userRoleService;
        this.userRepository = userRepository;
    }

    @Override
    public User register(User userToAdd) {
        final String username = userToAdd.getUsername();
        if (userRepository.findByUsername(username) != null) {
            return null;
        }
        return userRepository.save(userToAdd);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);

    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        return userRepository.save(user) != null;
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        userRepository.delete(id);
        return true;
    }
}
