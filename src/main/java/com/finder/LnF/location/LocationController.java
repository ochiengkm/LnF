package com.finder.LnF.location;

import com.finder.LnF.document.DocType;
import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@CrossOrigin
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/set-location")
    public ResponseEntity<?> setDocumentLocation(String documentNo, LocationDTO location) {
        return locationService.setDocumentLocation(documentNo, location);
    }

    @PatchMapping("/update-location")
    public ResponseEntity<?> updateDocumentLocation(String documentNo, LocationDTO location) {
        return locationService.setDocumentLocation(documentNo, location);
    }

    @GetMapping("/find-location")
    public ResponseEntity<?> getDocumentLocation(DocType docType, String documentNo) {
        return locationService.findDocumentLocation(docType, documentNo);
    }
}
