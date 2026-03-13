package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.controllers;

import jakarta.validation.Valid;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.Role;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.security.PersonDetails;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.RoleService;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal PersonDetails personDetails) {
        return ResponseEntity.ok(personDetails.getUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.removeUser(userService.getUser(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}