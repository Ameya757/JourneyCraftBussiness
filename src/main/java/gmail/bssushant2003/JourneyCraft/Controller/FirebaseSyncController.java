package gmail.bssushant2003.JourneyCraft.Controller;

import gmail.bssushant2003.JourneyCraft.Service.FirebaseSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/firebase-sync")
public class FirebaseSyncController {

    @Autowired
    private FirebaseSyncService firebaseSyncService;

    @PostMapping("/guides")
    public String syncGuides() {
        firebaseSyncService.syncGuides();
        return "Guides synced to Firebase!";
    }

    @PostMapping("/restaurants")
    public String syncRestaurants() {
        firebaseSyncService.syncRestaurants();
        return "Restaurants synced to Firebase!";
    }

    @PostMapping("/street-locations")
    public String syncStreetLocations() {
        firebaseSyncService.syncStreetLocations();
        return "Street locations synced to Firebase!";
    }

    @PostMapping("/all")
    public String syncAll() {
        firebaseSyncService.syncGuides();
        firebaseSyncService.syncRestaurants();
        firebaseSyncService.syncStreetLocations();
        return "All data synced to Firebase!";
    }
}

