package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services;

import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.dao.UserDao;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(long id) {
        return userDao.getUser(id);
    }

    @Transactional
    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.addUser(user);
    }

    @Transactional
    @Override
    public void removeUser(User user) {
        userDao.removeUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User updatedUser) {
        User existingUser = getUser(updatedUser.getId());
        if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(existingUser.getPassword());
        } else {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userDao.updateUser(updatedUser);
    }
}
