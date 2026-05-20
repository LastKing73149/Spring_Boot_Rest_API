package com.project.work.service;

import com.project.work.model.Role;
import com.project.work.model.User;
import com.project.work.repository.RoleRepository;
import com.project.work.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() != null) {
            Set<Role> managedRoles = user.getRoles().stream()
                    .map(role -> {
                        Role foundRole = roleRepository.findByName(role.getName());
                        if(foundRole == null) {
                            throw new RuntimeException("Роль не найдена в базе данных: " + role.getName());
                        }
                    return foundRole;
                    })
                    .collect(Collectors.toSet());
            user.setRoles(managedRoles);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Пользователь не найден"));

        User userWithSameName = userRepository.findByUsername(updatedUser.getUsername());
        if (userWithSameName != null && !userWithSameName.getId().equals(id)) {
            throw new RuntimeException("Никнейм '" + updatedUser.getUsername() + "' уже занят другим пользователем");
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setName(updatedUser.getName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setEmail(updatedUser.getEmail());

        if(updatedUser.getRoles() != null) {
            existingUser.getRoles().clear();

            Set<Role> managedRoles = updatedUser.getRoles().stream()
                    .map(role ->  {
                        Role foundRole = roleRepository.findByName(role.getName());
                        if(foundRole == null) {
                            throw new RuntimeException("Роль не найдена в базе данных: " + role.getName());
                        }
                        return foundRole;
                    })
                    .collect(Collectors.toSet());
            existingUser.getRoles().addAll(managedRoles);
        }

        if(updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()){
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
