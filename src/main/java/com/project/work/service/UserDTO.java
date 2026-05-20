package com.project.work.service;

import com.project.work.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String lastName;
    private Integer age;
    private String email;
    private Set<String> roles;

    public UserDTO() {}

    public UserDTO(Long id, String username, String name, String lastName, Integer age, String email, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public static UserDTO toDTO(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getAge(),
                user.getEmail(),
                roles
        );
    }
}
