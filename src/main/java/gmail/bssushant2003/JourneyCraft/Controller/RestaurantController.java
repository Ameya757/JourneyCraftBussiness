package gmail.bssushant2003.JourneyCraft.Controller;

import gmail.bssushant2003.JourneyCraft.Entity.Restaurant;
import gmail.bssushant2003.JourneyCraft.Entity.RestaurantDTO;
import gmail.bssushant2003.JourneyCraft.Entity.User;
import gmail.bssushant2003.JourneyCraft.Repository.UserRepository;
import gmail.bssushant2003.JourneyCraft.Repository.RestaurantRepository;
import gmail.bssushant2003.JourneyCraft.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {


    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    @PostMapping("/register/restaurant/{userId}")
    public ResponseEntity<Restaurant> registerRestaurant(@RequestBody RestaurantDTO restaurantDTO, @PathVariable Long userId){
        User user = userRepository.getReferenceById(userId);

        Restaurant restaurant = new Restaurant();
        restaurant.setUser(user);
        restaurant.setRestoName(restaurantDTO.getRestoName());
        restaurant.setRating(restaurantDTO.getRating());
        restaurant.setLocationLink(restaurantDTO.getLocationLink());
        restaurant.setFssaiLicense(restaurantDTO.getFssaiLicense());
        restaurant.setOpenTime(restaurantDTO.getOpenTime());
        restaurant.setCloseTime(restaurantDTO.getOpenTime());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setPhoneNo(restaurantDTO.getPhoneNo());
        restaurant.setAverageCost(restaurantDTO.getAverageCost());
        restaurant.setFoodType(restaurantDTO.getFoodType());

        Restaurant saveRestaurant = restaurantService.registerRestaurant(restaurant,userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveRestaurant);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id){
        return restaurantService.getRestaurantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all-restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants(){
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PutMapping("/update/{userId}")
    public void updateRestaurant(@RequestBody Restaurant resto, @PathVariable Long userId){
        Optional<Restaurant> restoFromDb = restaurantRepository.findByUserId(userId);
        if(restoFromDb.isPresent()){
            restaurantService.updateRestaurant(resto,userId);
        }
    }
    @DeleteMapping("/delete-restaurant/{id}")
    public ResponseEntity<?> deleteRestaurantById(@PathVariable Long id){
        restaurantService.deleteRestaurantById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
