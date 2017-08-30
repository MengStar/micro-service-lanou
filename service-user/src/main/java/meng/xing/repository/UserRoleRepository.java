package meng.xing.repository;

/**
 * Created by Administrator on 2017/7/17.
 */

import meng.xing.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    UserRole findByRole(String role);
}
