package com.finder.LnF.location;

import com.finder.LnF.document.DocRepository;
import com.finder.LnF.utils.ResponseEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final DocRepository docRepository;
    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<?> setDocumentLocation(String documentNo, LocationDTO locationReq){
        ResponseEntity<?> response = new ResponseEntity<>();
        var document = docRepository.findByDocumentNo(documentNo).orElseThrow(
                ()-> new NoSuchElementException("Please Capture the Document Details first!"));

        LocationDetails oldLocationDetails = document.getLocationDetails();
        if (oldLocationDetails != null){
            document.setLocationDetails(null);
            docRepository.save(document);
            entityManager.detach(document);
            locationRepository.delete(oldLocationDetails);
        }

        LocationDetails details =  LocationDetails.builder()
                .county(locationReq.getCounty())
                .subCounty(locationReq.getSubCounty())
                .location(locationReq.getLocation())
                .village(locationReq.getVillage())
                .nearestHudumaCentre(locationReq.getNearestHudumaCentre())
                .nearestGokFacility(locationReq.getNearestGokFacility())
                .build();
        try {
            locationRepository.save(details);
            document.setLocationDetails(details);
            docRepository.save(document);
            response.setMessage("Successfully updated");
            response.setStatusCode(200);
        } catch (Exception e) {
            throw new RuntimeException("Location could not be saved", e);
        }

        return response;
    }
}
