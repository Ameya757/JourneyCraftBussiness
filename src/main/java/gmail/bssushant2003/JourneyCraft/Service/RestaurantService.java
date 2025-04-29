package gmail.bssushant2003.JourneyCraft.Service;

import gmail.bssushant2003.JourneyCraft.Entity.Restaurant;
import gmail.bssushant2003.JourneyCraft.Repository.RestaurantRepository;
import gmail.bssushant2003.JourneyCraft.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import gmail.bssushant2003.JourneyCraft.Entity.User;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    public Restaurant registerRestaurant(Restaurant restaurant,Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setUser(user);
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> getRestaurantById(Long id){
        return restaurantRepository.findByUserId(id);
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }

    public void updateRestaurant(Restaurant resto, Long userId) {
        Optional<Restaurant> restoFromDb = restaurantRepository.findByUserId(userId);
        Restaurant toUpdateResto = restoFromDb.get();

        toUpdateResto.setAverageCost(resto.getAverageCost());
        toUpdateResto.setDescription(resto.getDescription());
        toUpdateResto.setRestoName(resto.getRestoName());
        toUpdateResto.setCloseTime(resto.getCloseTime());
        toUpdateResto.setPhoneNo(resto.getPhoneNo());
        toUpdateResto.setRating(resto.getRating());
        toUpdateResto.setOpenTime(resto.getOpenTime());
        toUpdateResto.setFssaiLicense(resto.getFssaiLicense());
        toUpdateResto.setLocationLink(resto.getLocationLink());
        toUpdateResto.setFoodType(resto.getFoodType());
        toUpdateResto.setApproved(resto.isApproved());

        restaurantRepository.save(toUpdateResto);
    }

    public void deleteRestaurantById(Long id){
        restaurantRepository.deleteById(id);
    }
}
