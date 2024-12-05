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
    public ResponseEntity<?> setDocumentLocation(@RequestParam String documentNo, @RequestBody LocationDTO location) {
        return locationService.setDocumentLocation(documentNo, location);
    }

    @PatchMapping("/update-location")
    public ResponseEntity<?> updateDocumentLocation(@RequestParam String documentNo, @RequestBody LocationDTO location) {
        return locationService.setDocumentLocation(documentNo, location);
    }

    @GetMapping("/find-location")
    public ResponseEntity<?> getDocumentLocation(@RequestParam DocType docType, @RequestParam String documentNo) {
        return locationService.findDocumentLocation(docType, documentNo);
    }
}
