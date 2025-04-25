package gmail.bssushant2003.JourneyCraft.Service;


import gmail.bssushant2003.JourneyCraft.Entity.LatLng;
import gmail.bssushant2003.JourneyCraft.Entity.StreetLocations;
import gmail.bssushant2003.JourneyCraft.Repository.StreetLocationRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LocationExtractorService {

    @Autowired
    private StreetLocationRepository streetLocationRepository;

    public Pair<Long,LatLng> extractLatLng(Long id, String googleMapsUrl) {
        try {
            // Fetch HTML content from the given URL
            Document doc = Jsoup.connect(googleMapsUrl).get();

            // Extract lat,lng from the URL inside the <meta> tags
            String html = doc.toString();
            Pattern pattern = Pattern.compile("!3d([-\\d.]+)!4d([-\\d.]+)");
            Matcher matcher = pattern.matcher(html);

            if (matcher.find()) {
                double latitude = Double.parseDouble(matcher.group(1));
                double longitude = Double.parseDouble(matcher.group(2));
                return Pair.of(id,new LatLng(latitude,longitude)) ;
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public Pair<Long,LatLng> extractLatLngforRestaurant(Long id, String googleMapsUrl) {
        try {
            // Fetch HTML content from the given URL
            Document doc = Jsoup.connect(googleMapsUrl).get();

            // Extract lat,lng from the URL inside the <meta> tags
            String html = doc.toString();
            Pattern pattern = Pattern.compile("!3d([-\\d.]+)!4d([-\\d.]+)");
            Matcher matcher = pattern.matcher(html);

            if (matcher.find()) {
                double latitude = Double.parseDouble(matcher.group(1));
                double longitude = Double.parseDouble(matcher.group(2));
                return Pair.of(id,new LatLng(latitude,longitude)) ;
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }


    public LatLng extractLatLngForPlaces(String googleMapsUrl) {
        try {
            // Fetch HTML content from the given URL
            Document doc = Jsoup.connect(googleMapsUrl).get();

            // Extract lat,lng from the URL inside the <meta> tags
            String html = doc.toString();
            Pattern pattern = Pattern.compile("!3d([-\\d.]+)!4d([-\\d.]+)");
            Matcher matcher = pattern.matcher(html);

            if (matcher.find()) {
                double latitude = Double.parseDouble(matcher.group(1));
                double longitude = Double.parseDouble(matcher.group(2));
                return new LatLng(latitude,longitude);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public StreetLocations getStreetLocations(@RequestParam StreetLocations streetLocations){
        LatLng streetlatlng = extractLatLngForPlaces(streetLocations.getLink());
        streetLocations.setLat(streetlatlng.getLat());
        streetLocations.setLng(streetlatlng.getLng());
        streetLocationRepository.save(streetLocations);
        return streetLocations;
    }

    public List<StreetLocations> getNearbyLocations(LatLng latLng) {
        return streetLocationRepository.findNearbyLocations(latLng.getLat(), latLng.getLng());
    }

}
