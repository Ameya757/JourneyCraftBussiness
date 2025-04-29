package gmail.bssushant2003.JourneyCraft.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LatLng {
    private double lat;
    private double lng;

    public LatLng(double latitude, double longitude) {
        lat = latitude;
        lng = longitude;
    }

    public double getLat() {
        return lat;
    }

    public double getLng(){
        return lng;
    }
}
