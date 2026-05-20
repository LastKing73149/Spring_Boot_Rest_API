package com.project.work.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    @NotEmpty(message = "Никнейм не должен быть пустым")
    @Size(min = 3, max = 20, message = "Никнейм должен быть от 3 до 20 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9._]+$", message = "Никнейм может содержать только буквы, цифры, точка и нижнее подчёркивание")
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    @NotEmpty(message = "Имя не должно быть пустым")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]+$", message = "Имя должно содержать только буквы")
    @Size(min = 2, max = 20, message = "Имя должно быть от 2 до 20 символов")
    private String name;

    @Column(name = "last_name")
    @NotEmpty(message = "Фамилия не должна быть пустой")
        @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]+$", message = "Фамилия должна содержать только буквы")
    @Size(min = 2, max = 20, message = "Фамилия должна быть от 2 до 20 символов")
    private String lastName;

    @Column(name = "age")
    @NotNull(message = "Укажите свой возраст")
    @Min(value = 1, message = "Возраст должен быть больше 0")
    @Max(value = 100, message = "Возраст не может быть больше 100")
    private Integer age;

    @Column(name = "email")
    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new java.util.HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(){

    }

    public User(String username, String password, Set<Role> roles, String name, String lastName, Integer age, String email){
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString(){
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
