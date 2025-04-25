package gmail.bssushant2003.JourneyCraft.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuideDTO {
    private Long userId;
    private String guidename;
    private int experience;
    private String language;
    private String bio;
    private Double latitude;
    private Double longitude;
    private String licenseNumber;
    private Boolean isAvailable;
    private String phoneNo;
}
