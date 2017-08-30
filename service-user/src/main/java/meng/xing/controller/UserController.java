package meng.xing.controller;

import meng.xing.entity.User;
import meng.xing.entity.UserRole;
import meng.xing.service.UserRoleService;
import meng.xing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
     UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Value("$(defaultUserRole)")
    String defaultUserRole;
    @GetMapping
    public Page<User> getUsers(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
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

    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        String username = request.get("username").toString();
        //用户名存在
        if (userService.findUserByUsername(username) != null) {
            response.put("message", "用户名已经存在");
            response.put("status", false);
            return response;
        }
        String password = request.get("password").toString();
        String nickName = request.get("nickName").toString();
        String phone = request.get("phone").toString();
        String email = request.get("email").toString();
        String address = request.get("address").toString();
        boolean female = (boolean) request.get("female");
        int age = (int) request.get("age");
        User user = new User(username, password, nickName, phone, email, address, female, age);
        userService.register(user);
        userService.setUserRoles(username,defaultUserRole);

        response.put("message", "用户注册成功");
        response.put("status", true);
        return response;
    }

    @GetMapping("/{username}")
    public Map<String, Object> getUserByUsername(@PathVariable("username") String username) {
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
    public boolean updateUserById(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {

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
    public boolean deleteUserById(@PathVariable("id") Long id) {
        return userService.deleteUserById(id);
    }

    @DeleteMapping
    public void deleteUsers(@RequestBody Map<String, ArrayList<Long>> map) {
        map.get("ids").forEach(id -> userService.deleteUserById(id));
    }
}
