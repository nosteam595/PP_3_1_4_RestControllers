package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services;

import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.repositories.UserRepository;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PersonDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user= userRepository.findByFirstName(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new PersonDetails(user.get());
    }
}
