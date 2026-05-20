package com.project.work.controller;

import com.project.work.model.Role;
import com.project.work.model.User;
import com.project.work.repository.UserRepository;
import com.project.work.service.RoleService;
import com.project.work.service.UserDTO;
import com.project.work.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    public final UserService userService;
    public final UserRepository userRepository;
    public final RoleService roleService;

    public AdminRestController(UserService userService, UserRepository userRepository, RoleService roleService){
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> dtoList = userService.getAllUsers().stream()
                .map(UserDTO::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/{id:[\\d]+}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "Пользователь с таким никнеймом уже существует!");
        }
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage,
                            (existing, replacement) -> existing
                    ));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if(userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(Map.of("username", "Этот никнейм уже занят"), HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping("/{id:[\\d]+}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user, BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage,
                            (existing, replacement) -> existing
                    ));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(id, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id:[\\d]+}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
