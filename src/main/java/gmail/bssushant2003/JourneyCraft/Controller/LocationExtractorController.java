package gmail.bssushant2003.JourneyCraft.Controller;


import gmail.bssushant2003.JourneyCraft.Entity.LatLng;
import gmail.bssushant2003.JourneyCraft.Entity.StreetLocations;
import gmail.bssushant2003.JourneyCraft.Service.LocationExtractorService;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationExtractorController {

    private final LocationExtractorService locationExtractorService;

    public LocationExtractorController(LocationExtractorService locationExtractorService) {
        this.locationExtractorService = locationExtractorService;
    }

    @GetMapping("/extract")
    public Pair<Long, LatLng> extractLatLng(@RequestParam String url, Long id) {
        return locationExtractorService.extractLatLng(id,url);
    }

    @GetMapping("/get-lat-lng")
    public LatLng getLatLngFromURL(@RequestParam String url){
        return locationExtractorService.extractLatLngForPlaces(url);
    }

    @PostMapping("/street-location")
    public StreetLocations getStreetLocation(@RequestBody StreetLocations streetLocations){
        return locationExtractorService.getStreetLocations(streetLocations);
    }

    @PostMapping("/nearby")
    public List<StreetLocations> getNearbyLocations(@RequestBody LatLng latLng) {
        List<StreetLocations> nearbyLocations = locationExtractorService.getNearbyLocations(latLng);
        return nearbyLocations;

    }
}
