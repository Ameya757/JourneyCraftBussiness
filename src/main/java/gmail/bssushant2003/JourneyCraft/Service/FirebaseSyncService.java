package gmail.bssushant2003.JourneyCraft.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import gmail.bssushant2003.JourneyCraft.Entity.*;
import gmail.bssushant2003.JourneyCraft.Repository.GuideRepository;
import gmail.bssushant2003.JourneyCraft.Repository.RestaurantRepository;
import gmail.bssushant2003.JourneyCraft.Repository.StreetLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter; // add this import at top


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirebaseSyncService {

    @Autowired
    private LocationExtractorService locationExtractorService;

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private StreetLocationRepository streetLocationRepository;

    public void syncGuides() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("guides");
        List<Guide> guides = guideRepository.findAll();
        ref.setValueAsync(null); // Clear existing

        for (Guide g : guides) {
            GuideDTO dto = new GuideDTO();
            dto.setUserId(g.getUser().getId());
            dto.setGuidename(g.getGuidename());
            dto.setExperience(g.getExperience());
            dto.setLanguage(g.getLanguage());
            dto.setBio(g.getBio());
            dto.setLatitude(g.getLatitude());
            dto.setLongitude(g.getLongitude());
            dto.setLicenseNumber(g.getLicenseNumber());
            dto.setIsAvailable(g.getIsAvailable());
            dto.setPhoneNo(g.getPhoneNo());

            ref.child(String.valueOf(dto.getUserId())).setValueAsync(dto);
        }
    }

    public void syncRestaurants() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("restaurants");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        ref.setValueAsync(null);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // or "hh:mm a" for AM/PM

        for (Restaurant r : restaurants) {
            Map<String, Object> dtoMap = new HashMap<>();
            dtoMap.put("userId", -r.getUser().getId());
            dtoMap.put("name", r.getRestoName());
            dtoMap.put("rating", r.getRating());
            dtoMap.put("locationLink", r.getLocationLink());
            dtoMap.put("fssaiLicense", r.getFssaiLicense());
            dtoMap.put("openTime", r.getOpenTime().format(timeFormatter));
            dtoMap.put("closeTime", r.getCloseTime().format(timeFormatter));
            dtoMap.put("description", r.getDescription());
            dtoMap.put("phoneNo", r.getPhoneNo());
            dtoMap.put("averageCost", r.getAverageCost());
            dtoMap.put("foodType", r.getFoodType());
            LatLng restlatlng = locationExtractorService.extractLatLngForPlaces(r.getLocationLink());
            dtoMap.put("latitude",restlatlng.getLat());
            dtoMap.put("longitude",restlatlng.getLng());


            ref.child(String.valueOf(r.getUser().getId())).setValueAsync(dtoMap);
        }
    }


    public void syncStreetLocations() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("streetLocations");
        List<StreetLocations> locations = streetLocationRepository.findAll();
        ref.setValueAsync(null);

        for (StreetLocations s : locations) {
            StreetLocationsDTO dto = new StreetLocationsDTO();
            dto.setName(s.getName());
            dto.setLat(s.getLat());
            dto.setLng(s.getLng());
            dto.setLink(s.getLink());

            ref.child(String.valueOf(s.getId())).setValueAsync(dto);
        }
    }
}
