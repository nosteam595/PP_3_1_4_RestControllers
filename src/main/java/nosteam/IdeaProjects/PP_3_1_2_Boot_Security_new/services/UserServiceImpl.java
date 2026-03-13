package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services;

import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.dao.UserDao;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> allUsers() {
        return userDao.allUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(long id) {
        return userDao.getUser(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserForProfile(User currentUser, Long id) {
        return userDao.getUserForProfile(currentUser, id);
    }

    @Transactional
    @Override
    public void addUser(User user, List<Long> roleIds) {
        userDao.addUser(user, roleIds);
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Transactional
    @Override
    public void removeUser(User user) {
        userDao.removeUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User updatedUser, List<Long> roleIds) {
        userDao.updateUser(updatedUser, roleIds);
    }

    @Override
    public void updateUser(User updatedUser) {
        userDao.updateUser(updatedUser);
    }

}
