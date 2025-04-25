package gmail.bssushant2003.JourneyCraft.Entity;

import gmail.bssushant2003.JourneyCraft.Controller.SyncTriggerListener;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "streetLocations")
@EntityListeners(SyncTriggerListener.class)
public class StreetLocations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    String name;

    @NonNull
    String link;

    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    double lat;

    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    double lng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
