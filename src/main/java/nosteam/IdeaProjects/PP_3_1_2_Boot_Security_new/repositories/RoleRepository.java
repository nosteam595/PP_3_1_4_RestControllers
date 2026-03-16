package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.repositories;

import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
