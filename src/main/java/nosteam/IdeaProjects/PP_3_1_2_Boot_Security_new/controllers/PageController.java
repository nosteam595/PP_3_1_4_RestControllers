package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/users")
    public String getAdminPage() {
        return "users";
    }

    @GetMapping("/users/user/details")
    public String getUserPage() {
        return "userProfile";
    }
}
