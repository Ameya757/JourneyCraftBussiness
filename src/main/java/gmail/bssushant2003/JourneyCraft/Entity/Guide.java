package gmail.bssushant2003.JourneyCraft.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gmail.bssushant2003.JourneyCraft.Controller.SyncTriggerListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "guide")
@EntityListeners(SyncTriggerListener.class)
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY) // Load user only when needed
    @JoinColumn(name = "user_id", nullable = false) // Foreign key reference
    private User user;

    @Column(nullable = false, length = 100)
    private String guidename;

    @Column(nullable = false)
    private int experience;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false, unique = true, length = 20)
    private String licenseNumber;

    private Boolean isAvailable;

    @Column(length = 10)
    private String phoneNo;
}
