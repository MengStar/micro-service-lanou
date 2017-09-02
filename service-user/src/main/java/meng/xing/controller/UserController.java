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

/**
 * API文档地址： http://localhost:2000/swagger-ui.html
 * 端口为该服务所在端口
 */
@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Value("${defaultUserRole}")
    private String[] defaultUserRole;

    @Autowired
    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public Page<User> getUsers(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "sort", defaultValue = "id") String sort,
                               @RequestParam(value = "order", defaultValue = "asc") String order) {
        Sort _sort = new Sort(Sort.Direction.fromString(order), sort);
        //传来的页码是从1开始，而服务器从1开始算
        Pageable pageable = new PageRequest(page - 1, pageSize, _sort);
        return userService.findAllUsers(pageable);
    }

    @PostMapping("/register")
    public ResponseStatus registerUser(@RequestBody RequestUser requestUser) {

        ResponseStatus response = new ResponseStatus();
        String username = requestUser.getUsername();
        //用户名存在
        if (userService.findUserByUsername(username) != null) {
            response.setMessage("用户名已经存在");
            response.setSuccess("false");
            return response;
        }
        User user = new User(requestUser.getUsername(), requestUser.getPassword(),
                requestUser.getNickName(), requestUser.getPhone(), requestUser.getEmail(),
                requestUser.getAddress(), requestUser.isFemale(), requestUser.getAge());
        Set<UserRole> _roles = new HashSet<>();
        for (String role : defaultUserRole) {
            _roles.add(userRoleService.findUserRoleByRole(role));
        }
        user.setRoles(_roles);
        userService.register(user);
        response.setMessage("用户注册成功");
        response.setSuccess("true");
        return response;

    }

    @GetMapping("/{username}")
    public ResponseUser getUserByUsername(@PathVariable("username") String username) {
        ResponseUser _responseUser = new ResponseUser();
        User _user = userService.findUserByUsername(username);
        _responseUser.setId(_user.getId().toString());
        _responseUser.setUsername(_user.getUsername());
        _ResponsePermissions permissions = new _ResponsePermissions();
        permissions.setRoles(_user.getRoles().stream().map(UserRole::getRole).collect(Collectors.toList()));
        permissions.setVisit("1,3,4,5");//todo这是控制菜单的路径，有时间移动后台
        _responseUser.setPermissions(permissions);
        return _responseUser;
    }

    @GetMapping("/{username}/password")
    public String getPasswordByUsername(@PathVariable("username") String username) {
        return userService.findUserByUsername(username).getPassword();
    }

    @RequestMapping(value = "/{username}/lastPasswordResetDate", method = RequestMethod.GET)
    public Date getLastPasswordResetByUsername(@PathVariable("username") String username) {
        return userService.findUserByUsername(username).getLastPasswordResetDate();
    }

    @PatchMapping("/{id}")
    public ResponseStatus updateUserById(@PathVariable("id") Long id, @RequestBody RequestUser requestUser) {
        ResponseStatus responseStatus = new ResponseStatus();
        User user = userService.findUserById(id);

        List<String> roles = requestUser.getRoles();
        if (roles != null) {
            Set<UserRole> _roles = new HashSet<>();
            roles.forEach(
                    (role) -> _roles.add(userRoleService.findUserRoleByRole(role)));
            user.setRoles(_roles);
        }

        user.setNickName(requestUser.getNickName());
        user.setFemale(requestUser.isFemale());
        user.setAge(requestUser.getAge());
        user.setAddress(requestUser.getAddress());
        user.setEmail(requestUser.getEmail());
        user.setPhone(requestUser.getPhone());

        if (userService.updateUser(user)) {
            responseStatus.setSuccess("true");
            responseStatus.setMessage("用户更新成功");
            return responseStatus;
        }
        responseStatus.setSuccess("false");
        responseStatus.setMessage("用户更新失败");
        return responseStatus;
    }

    @DeleteMapping("/{id}")
    public ResponseStatus deleteUserById(@PathVariable("id") Long id) {
        ResponseStatus responseStatus = new ResponseStatus();
        if (userService.deleteUserById(id)) {
            responseStatus.setSuccess("true");
            responseStatus.setMessage("用户更新成功");
            return responseStatus;
        }
        responseStatus.setSuccess("false");
        responseStatus.setMessage("用户更新失败");
        return responseStatus;
    }

    @DeleteMapping
    public ResponseStatus deleteUsers(@RequestBody RequestIds ids) {

        List<Long> _ids = ids.getIds();
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            _ids.forEach(userService::deleteUserById);
            responseStatus.setMessage("批量删除用户成功!ids:" + _ids);
            responseStatus.setSuccess("true");
            return responseStatus;
        } catch (Exception e) {
            responseStatus.setMessage("批量删除用户失败!ids:" + _ids);
            responseStatus.setSuccess("false");
            return responseStatus;
        }
    }

    @GetMapping("userRoles")
    public List<UserRole> getUserRoles() {
        return userRoleService.findALlRoles();
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
        private List<String> roles;

        public RequestUser() {

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

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }

    class RequestIds {
        private List<Long> ids;

        public RequestIds() {
        }

        public List<Long> getIds() {
            return ids;
        }

        public void setIds(List<Long> ids) {
            this.ids = ids;
        }
    }

    class ResponseStatus {
        private String success;
        private String message;

        public ResponseStatus() {
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


    class ResponseUser {
        private String id;
        private String username;
        private _ResponsePermissions permissions;

        public ResponseUser() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public _ResponsePermissions getPermissions() {
            return permissions;
        }

        public void setPermissions(_ResponsePermissions permissions) {
            this.permissions = permissions;
        }
    }

    class _ResponsePermissions {
        private List<String> roles;
        private String visit;

        public _ResponsePermissions() {
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public String getVisit() {
            return visit;
        }

        public void setVisit(String visit) {
            this.visit = visit;
        }
    }
}

