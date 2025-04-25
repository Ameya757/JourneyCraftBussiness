package gmail.bssushant2003.JourneyCraft.Repository;

import gmail.bssushant2003.JourneyCraft.Entity.Guide;
import gmail.bssushant2003.JourneyCraft.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide,Long> {

    Optional<Guide> findByUserId(Long id);
    Optional<Guide> findByUser(User user);
}
