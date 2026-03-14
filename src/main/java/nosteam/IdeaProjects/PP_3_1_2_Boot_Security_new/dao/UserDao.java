package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.dao;

import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUser(long id);
    void addUser(User user);
    void removeUser(User user);
    void updateUser(User updatedUser);
}
