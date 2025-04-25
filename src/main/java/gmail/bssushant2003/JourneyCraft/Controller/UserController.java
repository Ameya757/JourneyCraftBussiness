package gmail.bssushant2003.JourneyCraft.Controller;

import gmail.bssushant2003.JourneyCraft.Entity.User;
import gmail.bssushant2003.JourneyCraft.Entity.UserDetailsDTO;
import gmail.bssushant2003.JourneyCraft.Service.EmailService;
import gmail.bssushant2003.JourneyCraft.Service.OtpService;
import gmail.bssushant2003.JourneyCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gmail.bssushant2003.JourneyCraft.DTO.OtpRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @GetMapping("all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAll();
        if(users != null && !users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUserByEmail(@RequestParam String email){
        Optional<User> user = userService.findByEmail(email);
        return user.map(value-> new ResponseEntity<>(value,HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> authenticatedUser = userService.loginUser(user.getEmail(), user.getPassword());

        if (authenticatedUser.isPresent()) {
            User loggedInUser = authenticatedUser.get();

            return ResponseEntity.ok(Map.of(
                    "id",loggedInUser.getId(),
                    "username", loggedInUser.getUsername(),
                    "email", loggedInUser.getEmail(),
                    "role", loggedInUser.getRole(),
                    "message", "Login successful"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Credentials. Try again"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        try{
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDetailsDTO>> getUserByRole(@PathVariable User.Role role){
        return ResponseEntity.ok(userService.gerUserByRole(role));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequest request) {
        System.out.println(request.getEmail());
        otpService.generateAndSendOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent successfully.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest request) {

        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }
}
