package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        System.out.println("Роли пользователя: " + roles);

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/users");
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/users/user/details");
        } else {
            response.sendRedirect("/");
        }
    }
}