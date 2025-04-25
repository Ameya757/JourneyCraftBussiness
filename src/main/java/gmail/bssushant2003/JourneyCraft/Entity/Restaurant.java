package gmail.bssushant2003.JourneyCraft.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import gmail.bssushant2003.JourneyCraft.Controller.SyncTriggerListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant")
@EntityListeners(SyncTriggerListener.class)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY) // Load user only when needed
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String restoName;

    @Column
    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String locationLink;

    @Column(nullable = false, unique = true, length = 14)
    private String fssaiLicense;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 10)
    private String phoneNo;

    private Double averageCost;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private FoodType foodType;

    public enum FoodType {
        VEG, NON_VEG , BOTH
    }
}
