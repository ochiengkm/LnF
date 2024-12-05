package com.finder.LnF.contact;

import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/document")
public class ContactController {
    private final ContactService contactService;

    @PostMapping("/set-contact")
    public ResponseEntity<?> setContact(@RequestParam String documentNo, @RequestBody ContactDTO contact) {
        return contactService.setContact(documentNo, contact);
    }
}
