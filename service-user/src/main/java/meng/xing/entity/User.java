package meng.xing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity //jpa的标签 根据字段，自动创表
@Table(name = "user") //生成的表名
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//jpa 主键和自增
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    @JsonIgnore
    private String password; //转json 忽略password
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastPasswordResetDate;
    @NotNull
    private String nickName;
    @NotNull
    private int age;
    @NotNull
    private boolean isFemale;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String address;
    @NotNull
    private String phone;

    @OneToMany
    @NotFound
    @JoinColumn(name = "roles")
    private Set<UserRole> roles;

    protected User() {
    }

    /**
     * 新建User
     *
     * @param username 用户名
     * @param password 密码
     */
    public User(String username, String password,
                String nickName, String phone, String email, String address, boolean isFemale, int age) {
        this.username = username;
        this.password = password;
        Date date = new Date();
        this.createDate = date;
        this.lastPasswordResetDate = date;
        this.age = age;
        this.nickName = nickName;
        this.email = email;
        this.address = address;
        this.isFemale = isFemale;
        this.phone = phone;
        this.roles = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setFemale(boolean female) {
        isFemale = female;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}