package gmail.bssushant2003.JourneyCraft.Controller;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SyncTriggerListener {


    @Autowired
    private RestTemplate restTemplate;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void onAnyChange(Object entity) {
        try {
            restTemplate.postForEntity("http://localhost:8081/api/firebase-sync/all", null, Void.class);
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log this
        }
    }
}

