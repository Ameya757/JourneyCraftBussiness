package gmail.bssushant2003.JourneyCraft.Controller;

import gmail.bssushant2003.JourneyCraft.Entity.Guide;
import gmail.bssushant2003.JourneyCraft.Entity.GuideDTO;
import gmail.bssushant2003.JourneyCraft.Entity.User;
import gmail.bssushant2003.JourneyCraft.Repository.UserRepository;
import gmail.bssushant2003.JourneyCraft.Repository.GuideRepository;
import gmail.bssushant2003.JourneyCraft.Service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5501")
@RestController
@RequestMapping("/api/guides")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuideRepository guideRepository;

    @PostMapping("/register/guide/{userId}")
    public ResponseEntity<Guide> registerGuide(@RequestBody GuideDTO guideDTO, @PathVariable Long userId){
        User user = userRepository.getReferenceById(userId);

        Guide guide = new Guide();
        guide.setUser(user);
        guide.setGuidename(guideDTO.getGuidename());
        guide.setExperience(guideDTO.getExperience());
        guide.setLanguage(guideDTO.getLanguage());
        guide.setBio(guideDTO.getBio());
        guide.setLatitude(guideDTO.getLatitude());
        guide.setLongitude(guideDTO.getLongitude());
        guide.setLicenseNumber(guideDTO.getLicenseNumber());
        guide.setIsAvailable(guideDTO.getIsAvailable());
        guide.setPhoneNo(guideDTO.getPhoneNo());

        Guide savedGuide = guideService.registerGuide(guide,userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedGuide);
    }

    @GetMapping("/guide/{id}")
    public ResponseEntity<Guide> getGuideById(@PathVariable Long id){
        return guideService.getGuideById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all-guides")
    public ResponseEntity<List<Guide>> getAllGuides(){
        return ResponseEntity.ok(guideService.getAllGuides());
    }

    @PutMapping("/update/{userId}")
    public void updateGuide(@RequestBody Guide guide, @PathVariable Long userId){
        Optional<Guide> guideFromDb = guideRepository.findByUserId(userId);
        if(guideFromDb.isPresent()){
            guideService.updateGuide(guide,userId);
        }
    }

    @DeleteMapping("/delete-guide/{id}")
    public ResponseEntity<?> deleteGuideById(@PathVariable Long id){
        guideService.deleteGuideById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
