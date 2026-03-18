package com.varsha.disastermanagement.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.varsha.disastermanagement.disaster.DisasterEvent;
import com.varsha.disastermanagement.disaster.DisasterType;
import com.varsha.disastermanagement.disaster.SeverityLevel;
import com.varsha.disastermanagement.disaster.Status;

@Service
public class DisasterApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DisasterEventService disasterEventService;

    private static final String USGS_EARTHQUAKE_API = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    @Scheduled(fixedRate = 300000) // 5 minutes
    @SuppressWarnings("all")
    public void fetchEarthquakeData() {
        try {
            String response = restTemplate.getForObject(USGS_EARTHQUAKE_API, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode features = root.path("features");

            for (JsonNode feature : features) {
                JsonNode properties = feature.path("properties");
                JsonNode geometry = feature.path("geometry");

                double magnitude = properties.path("mag").asDouble();
                String place = properties.path("place").asText();
                JsonNode coordinates = geometry.path("coordinates");
                double longitude = coordinates.get(0).asDouble();
                double latitude = coordinates.get(1).asDouble();

                // Map to DisasterEvent
                DisasterEvent disasterEvent = new DisasterEvent();
                disasterEvent.setTitle("Earthquake: " + place);
                disasterEvent.setDescription("Magnitude: " + magnitude + ", Location: " + place);
                disasterEvent.setDisasterType(DisasterType.EARTHQUAKE);
                disasterEvent.setSeverity(magnitude > 5.0 ? SeverityLevel.CRITICAL : SeverityLevel.MEDIUM);
                disasterEvent.setLocationName(place);
                disasterEvent.setLatitude(latitude);
                disasterEvent.setLongitude(longitude);
                disasterEvent.setStatus(Status.PENDING);
                disasterEvent.setCreatedAt(LocalDateTime.now());

                disasterEventService.saveDisasterEvent(disasterEvent);
            }
        } catch (RestClientException e) {
            // Log error
            System.err.println("Error fetching earthquake data: " + e.getMessage());
        } catch (JsonProcessingException e) {
            // Log error
            System.err.println("Error fetching earthquake data: " + e.getMessage());
        }
    }
}