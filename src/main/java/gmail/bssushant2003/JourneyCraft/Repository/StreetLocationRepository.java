package gmail.bssushant2003.JourneyCraft.Repository;

import gmail.bssushant2003.JourneyCraft.Entity.StreetLocations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreetLocationRepository extends JpaRepository<StreetLocations,Long> {

        @Query(value = "SELECT * FROM ( " +
                "SELECT *, (6371000 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(lat)) * " +
                "COS(RADIANS(lng) - RADIANS(:lng)) + SIN(RADIANS(:lat)) * SIN(RADIANS(lat)))) AS distance " +
                "FROM street_locations ) temp WHERE distance <= 500", nativeQuery = true)
        List<StreetLocations> findNearbyLocations(@Param("lat") double lat, @Param("lng") double lng);


}
