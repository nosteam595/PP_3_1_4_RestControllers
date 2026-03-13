package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.Role;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.repositories.PeopleRepository;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.RoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PeopleRepository peopleRepository;

    public UserDaoImpl(RoleService roleService, PasswordEncoder passwordEncoder, PeopleRepository peopleRepository) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.peopleRepository = peopleRepository;
    }

    @Override
    public List<User> allUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserForProfile(User currentUser, Long id) {
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (id != null && isAdmin) {
            return getUser(id);
        }
        return currentUser;
    }

    @Override
    public void addUser(User user, List<Long> roleIds) {
        // 1. Привязываем роли
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleIds.stream()
                    .map(roleService::getRoleById)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        peopleRepository.save(user);
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        peopleRepository.save(user);
    }

    @Override
    public void removeUser(User user) {
        entityManager.remove(entityManager.merge(user));
    }

    @Override
    public void updateUser(User updatedUser, List<Long> roleIds) {
        User existingUser = getUser(updatedUser.getId());
        if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(existingUser.getPassword());
        } else {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleIds.stream()
                    .map(roleService::getRoleById)
                    .collect(Collectors.toSet());
            updatedUser.setRoles(roles);
        } else {
            updatedUser.setRoles(existingUser.getRoles());
        }
        peopleRepository.save(updatedUser);
    }

    @Override
    public void updateUser(User updatedUser) {
        User existingUser = getUser(updatedUser.getId());
        if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(existingUser.getPassword());
        } else {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
//        if (roleIds != null && !roleIds.isEmpty()) {
//            Set<Role> roles = roleIds.stream()
//                    .map(roleService::getRoleById)
//                    .collect(Collectors.toSet());
//            updatedUser.setRoles(roles);
//        } else {
//            updatedUser.setRoles(existingUser.getRoles());
//        }
        peopleRepository.save(updatedUser);
    }
}
