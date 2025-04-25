package gmail.bssushant2003.JourneyCraft.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantDTO {
    private Long userId;
    private String restoName;
    private Double rating;
    private String locationLink;
    private String fssaiLicense;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String description;
    private String phoneNo;
    private Double averageCost;
    private Restaurant.FoodType foodType;
}
