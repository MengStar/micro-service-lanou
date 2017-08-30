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
    final UserService userService;
    final UserRoleService userRoleService;
    @Value("${defaultUserRole}")
    String defaultUserRole;

    @Autowired
    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public Page<User> getUsers(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
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
    public Map<String, Object> registerUser(@RequestBody RequestUser requestUser) {

        Map<String, Object> response = new HashMap<>();
        String username = requestUser.getUsername();
        //用户名存在
        if (userService.findUserByUsername(username) != null) {
            response.put("message", "用户名已经存在");
            response.put("status", false);
            return response;
        }
        userService.register(new User(requestUser.getUsername(), requestUser.getPassword(),
                requestUser.getNickName(), requestUser.getPhone(), requestUser.getEmail(),
                requestUser.getAddress(), requestUser.isFemale(), requestUser.getAge()));

        userService.setUserRoles(username, defaultUserRole);
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

class RequestUser {
    private String username;
    private String password;
    private String nickName;
    private String phone;
    private String email;
    private String address;
    private boolean female;
    private int age;

    protected RequestUser() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}