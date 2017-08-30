package meng.xing.service;

import meng.xing.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {


    User findUserByUsername(String username);
    User findUserById(Long id);
    Page<User> findAllUsers(Pageable pageable);

    boolean setUserRoles(String username, String... roles);

    boolean updateUser(User user);
    boolean deleteUserById(Long id);
}
