package com.finder.LnF.contact;

import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/document")
public class ContactController {
    private final ContactService contactService;

    @PostMapping("/set-contact")
    public ResponseEntity<?> setContact(String documentNo, ContactDTO contact) {
        return contactService.setContact(documentNo, contact);
    }
}
