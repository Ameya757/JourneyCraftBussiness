package gmail.bssushant2003.JourneyCraft.Service;

import gmail.bssushant2003.JourneyCraft.Entity.User;
import gmail.bssushant2003.JourneyCraft.Entity.UserDetailsDTO;
import gmail.bssushant2003.JourneyCraft.Repository.GuideRepository;
import gmail.bssushant2003.JourneyCraft.Repository.RestaurantRepository;
import gmail.bssushant2003.JourneyCraft.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<UserDetailsDTO> gerUserByRole(User.Role role){
        List<User> users = userRepository.findByRole(role);
        List<UserDetailsDTO> userDetailsList = new ArrayList<>();

        for(User user : users){
            if(role == User.Role.GUIDE){
                guideRepository.findByUser(user).ifPresent(guide ->
                        userDetailsList.add(new UserDetailsDTO(
                                user.getId(), user.getEmail(), guide.getGuidename(), guide.getPhoneNo(),
                                "Experience: " + guide.getExperience() + ", License: " + guide.getLicenseNumber()
                        ))
                );
            } else if (role == User.Role.RESTAURANT) {
                restaurantRepository.findByUser(user).ifPresent(restaurant ->
                        userDetailsList.add(new UserDetailsDTO(
                                user.getId(), user.getEmail(), restaurant.getRestoName(), restaurant.getPhoneNo(),
                                "Rating: " + restaurant.getRating() + ", Food Type: " + restaurant.getFoodType()
                        ))
                );
            }
        }
        return userDetailsList;
    }

    public Optional<User> loginUser(String email, String password) {
        Optional<User> userFromDb = userRepository.findByEmail(email);
        if (userFromDb.isPresent() && passwordEncoder.matches(password, userFromDb.get().getPassword())) {
            return userFromDb;  // Password is valid
        }
        return Optional.empty();  // Invalid credentials
    }

    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public void deleteUserAndRelations(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            if(user.getRole() == User.Role.GUIDE){
                guideRepository.findByUser(user).ifPresent(guideRepository::delete);
            }else if(user.getRole() == User.Role.RESTAURANT){
                restaurantRepository.findByUser(user).ifPresent(restaurantRepository::delete);
            }

            userRepository.delete(user);
        }else{
            throw new RuntimeException("User with ID " + userId + " Not Found");
        }
    }

}
