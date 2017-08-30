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
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

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
    public boolean setUserRoles(String username, String... roles) {

        Set<UserRole> _roles = new HashSet<>();
        for (int i = 0; i < roles.length; i++) {
            _roles.add(userRoleRepository.findByRole(roles[i]));
        }
        User user = userRepository.findByUsername(username);
        logger.info(_roles.toString());
        user.setRoles(_roles);
        return userRepository.save(user) != null;
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        if (userRepository.save(user) != null)
            return true;
        else return false;
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        userRepository.delete(id);
        return true;
    }
}
