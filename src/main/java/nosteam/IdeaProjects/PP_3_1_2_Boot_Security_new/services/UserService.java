package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services;

import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import java.util.List;

public interface UserService {
    List<User> allUsers();
    User getUser(long id);
    void addUser(User user);
    void removeUser(User user);
    void updateUser(User updatedUser);
}
