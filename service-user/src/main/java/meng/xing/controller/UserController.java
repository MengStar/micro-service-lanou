package meng.xing.controller;


import meng.xing.entity.User;
import meng.xing.entity.UserRole;
import meng.xing.service.UserRoleService;
import meng.xing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/info")
    public String info(@RequestParam(value = "username",required = false) String username, @RequestParam(value = "accessToken") String[] token) {
        return username + token[0];
    }

    @GetMapping
    public Page<User> getAllUsers(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "sort", defaultValue = "id") String sort,
                                  @RequestParam(value = "order", defaultValue = "asc") String order,
                                  @RequestParam(value = "username", required = false) String username) {
        System.out.println(username);
        Sort _sort = new Sort(Sort.Direction.fromString(order), sort);
        //传来的页码是从1开始，而服务器从1开始算
        Pageable pageable = new PageRequest(page - 1, pageSize, _sort);
        return userService.findAllUsers(pageable);
    }


    @GetMapping("/{username}")
    public Map<String, Object> getUserByPathVariableUsername(@PathVariable("username") String username) {
        User _user = userService.findUserByUsername(username);
        Map<String, Object> user = new HashMap<>();
        user.put("id", _user.getId());
        user.put("username", _user.getUsername());
        Map<String, Object> permissions = new HashMap<>();
        permissions.put("roles", _user.getRoles().stream().map(userRole -> userRole.getRole()).collect(Collectors.toList()));
        //todo这是控制菜单的路径，有时间移动后台
        permissions.put("visit", "1,3,4,5");
        user.put("permissions", permissions);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        return data;
    }

    @PatchMapping("/{id}")
    public boolean update(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {

        User user = userService.findUserById(id);

        ArrayList roles = (ArrayList) map.get("roles");
        if (roles != null) {
            Set<UserRole> _roles = new HashSet<>();
            roles.forEach(
                    (role) -> _roles.add(userRoleService.findUserRoleByRole((String) role)));
            user.setRoles(_roles);
        }

        user.setNickName(map.get("nickName").toString());
        user.setFemale((boolean) map.get("female"));
        user.setAge((int) map.get("age"));
        user.setAddress(map.get("address").toString());
        user.setEmail(map.get("email").toString());
        user.setPhone(map.get("phone").toString());
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return userService.deleteUserById(id);
    }

    @DeleteMapping
    public void delete(@RequestBody Map<String, ArrayList<Long>> map) {
        map.get("ids").forEach(id -> userService.deleteUserById(id));
    }
}
