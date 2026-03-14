package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.repositories;

import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.firstName = :username")
    Optional<User> findByFirstName(@Param("username") String username);

}