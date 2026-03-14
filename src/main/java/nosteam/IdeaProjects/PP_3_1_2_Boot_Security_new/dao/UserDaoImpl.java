package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void removeUser(User user) {
        entityManager.remove(entityManager.merge(user));
    }

    @Override
    public void updateUser(User updatedUser) {
        entityManager.merge(updatedUser);
    }

}
