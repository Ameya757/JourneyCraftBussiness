package gmail.bssushant2003.JourneyCraft.Service;

import gmail.bssushant2003.JourneyCraft.Entity.Guide;
import gmail.bssushant2003.JourneyCraft.Entity.User;
import gmail.bssushant2003.JourneyCraft.Repository.GuideRepository;
import gmail.bssushant2003.JourneyCraft.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GuideService {


    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private UserRepository userRepository;

    public Guide registerGuide(Guide guide,Long userId){
        User user = userRepository.getReferenceById(userId);
        guide.setUser(user);
        return guideRepository.save(guide);
    }

    public Optional<Guide> getGuideById(Long id){
        return guideRepository.findByUserId(id);
    }

    public List<Guide> getAllGuides(){return guideRepository.findAll();}

    public void updateGuide(Guide guide , Long userId) {
        Optional<Guide> guideFromDb = guideRepository.findByUserId(userId);
        Guide toUpdateGuide = guideFromDb.get();
        toUpdateGuide.setGuidename(guide.getGuidename());
        toUpdateGuide.setExperience(guide.getExperience());
        toUpdateGuide.setLatitude(guide.getLatitude());
        toUpdateGuide.setBio(guide.getBio());
        toUpdateGuide.setPhoneNo(guide.getPhoneNo());
        toUpdateGuide.setLongitude(guide.getLongitude());
        toUpdateGuide.setIsAvailable(guide.getIsAvailable());
        toUpdateGuide.setLanguage(guide.getLanguage());
        toUpdateGuide.setLicenseNumber(guide.getLicenseNumber());
        guideRepository.save(toUpdateGuide);
    }

    public void deleteGuideById(Long id){guideRepository.deleteById(id);}


}
