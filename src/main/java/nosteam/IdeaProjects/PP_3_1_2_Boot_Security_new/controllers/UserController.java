package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.controllers;

import jakarta.validation.Valid;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.security.PersonDetails;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.RoleService;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users";
    }

    @GetMapping("/user/details")
    public String showUserProfile(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        model.addAttribute("user", personDetails.getUser());
        return "userProfile";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("newUser") @Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "listRoles", required = false) List<Long> roleIds,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.allUsers());
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "users";
        }
        userService.addUser(user, roleIds);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "listRoles", required = false) List<Long> roleIds) {
        userService.updateUser(user, roleIds);
        return "redirect:/users";
    }

    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id) {
        userService.removeUser(userService.getUser(id));
        return "redirect:/users";
    }
}
