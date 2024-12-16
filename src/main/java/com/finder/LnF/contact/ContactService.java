package com.finder.LnF.contact;

import com.finder.LnF.document.DocRepository;
import com.finder.LnF.utils.ResponseEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {
    private final DocRepository docRepository;

    @Transactional
    public ResponseEntity<?> setContact(String documentNo, ContactDTO contactDTO) {

        String username;
        try {
            username = getPrincipal();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<?> response = new ResponseEntity<>();
        var document = docRepository.findByDocumentNo(documentNo).orElseThrow(
                ()-> new NoSuchElementException("Document not found"));
        var contact = document.getContact();

        try {
            if (contact == null) {
                contact = Contact.builder()
                        .fullName(contactDTO.getFullName())
                        .phoneNumber(contactDTO.getPhoneNumber())
                        .email(contactDTO.getEmail())
                        .contactFlag(contactDTO.getContactFlag())
                        .username(username)
                        .build();

                contact.setDoc(document);
                document.setContact(contact);

                docRepository.save(document);
                response.setMessage("Contact added successfully");
            }
            else {
                contact.setFullName(contactDTO.getFullName());
                contact.setPhoneNumber(contactDTO.getPhoneNumber());
                contact.setEmail(contactDTO.getEmail());
                contact.setContactFlag(contactDTO.getContactFlag());

                contact.setDoc(document);
                document.setContact(contact);

                docRepository.save(document);
                response.setMessage("Contact updated successfully");
            }
            response.setStatusCode(200);
        } catch (Exception e) {
            log.error("Error updating contact for document: {}", documentNo, e);
            throw new RuntimeException("Contact Update Failed", e);
        }
        return response;
    }

    private String getPrincipal() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Kindly login first");
        }
        return authentication.getName();
    }
}
