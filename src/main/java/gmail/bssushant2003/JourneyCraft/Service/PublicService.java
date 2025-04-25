package gmail.bssushant2003.JourneyCraft.Service;

import gmail.bssushant2003.JourneyCraft.Entity.Guide;
import gmail.bssushant2003.JourneyCraft.Entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PublicService {


    private RestTemplate restTemplate;

    public PublicService(){
        this.restTemplate = new RestTemplate();
    }

    public List<Guide> fetchAllGuides(){
        String url = "http://localhost:8080/public/all-guides";
        ResponseEntity<Guide[]> response = restTemplate.getForEntity(url,Guide[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public List<Restaurant> fetchAllRestaurants(){
        String url = "http://localhost:8080/public/all-restaurants";
        ResponseEntity<Restaurant[]> response = restTemplate.getForEntity(url,Restaurant[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

}
