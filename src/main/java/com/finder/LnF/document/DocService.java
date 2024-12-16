package com.finder.LnF.document;

import com.finder.LnF.contact.ContactRepository;
import com.finder.LnF.location.LocationDetails;
import com.finder.LnF.location.LocationRepository;
import com.finder.LnF.mailService.EmailService;
import com.finder.LnF.utils.ResponseEntity;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocService {
    private final DocRepository docRepository;
    private final LocationRepository locationRepository;
    private final EmailService emailService;
    private final ContactRepository contactRepository;

    public ResponseEntity<Doc> captureDoc(DocDTO docRequest, DocType docType) {

        String username;
        try {
            username = getPrincipal();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<Doc> response = new ResponseEntity<>();
        try {
            if (docRepository.findByDocumentNo(docRequest.getDocumentNo()).isEmpty()) {
                Doc doc =  Doc.builder()
                        .documentType(docType)
                        .documentNo(docRequest.getDocumentNo())
                        .dob(docRequest.getDob())
                        .officialDocumentNames(docRequest.getOfficialDocumentNames())
                        .description(docRequest.getDescription())
                        .uploadedBy(username)
                        .build();

                response.setEntity(docRepository.save(doc));
                response.setStatusCode(201);
                response.setMessage("Successfully captured document " + doc.getDocumentNo());
            } else{
                log.warn("Document already exists");
                response.setMessage("Document already exists!");
                response.setStatusCode(400);
                response.setEntity(null);
            }
        } catch (Exception e) {
            log.error("Error capturing Doc", e);
            response.setStatusCode(500);
        }
        return response;
    }




    public ResponseEntity<Doc> findDocument(DocType docType, String documentNo) {
        ResponseEntity<Doc> response = new ResponseEntity<>();
        Optional<Doc> doc = docRepository.findByDocumentTypeAndDocumentNo(docType, documentNo);
        try {
            if (doc.isPresent()) {
                String document = docType.toString().replace("_", " ");
                response.setMessage(document + " Number: " + doc.get().getDocumentNo() + " found");
                response.setStatusCode(200);
                response.setEntity(doc.get());
            }
            else {
                response.setMessage("Document Number: " + documentNo + " not found");
                response.setStatusCode(404);
                response.setEntity(null);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ResponseEntity<Doc> deleteDocument(String documentNo) {
        ResponseEntity<Doc> response = new ResponseEntity<>();
        Optional<Doc> doc = docRepository.findByDocumentNo(documentNo);
        try {
            if (doc.isPresent()) {
                LocationDetails location = doc.get().getLocationDetails();

                if (location != null) {
                    locationRepository.delete(location);
                }
                docRepository.delete(doc.get());
                response.setMessage("Document deleted");
                response.setStatusCode(200);
            } else {
                response.setMessage("Document not found");
                response.setStatusCode(404);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ResponseEntity<Doc> updateDocument(DocDTO docDTO, String documentNo){// Was not Updating properly
        ResponseEntity<Doc> response = new ResponseEntity<>();
        Optional<Doc> doc = docRepository.findByDocumentNo(documentNo);

        try {
            if (doc.isPresent()) {
                var updatedDoc = doc.get();
                updatedDoc = Doc.builder()
                        .documentNo(docDTO.getDocumentNo())
                        .dob(docDTO.getDob())
                        .officialDocumentNames(docDTO.getOfficialDocumentNames())
                        .description(docDTO.getDescription())
                        .build();

                docRepository.save(updatedDoc);
                response.setMessage("Document details updated successfully");
                response.setStatusCode(200);
            }
            else {
                response.setMessage("Document Does not exist");
                response.setStatusCode(400);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public ResponseEntity<?> requestDocument(LocalDate dob) throws MessagingException {

        String userId;
        try {
            userId = getPrincipal();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }


        var contactDetails = contactRepository.findByUsername(userId).orElse(null);
        String email = Objects.requireNonNull(contactDetails).getEmail();

        var doc = docRepository.findDocByDob(dob).filter(document
                -> document.getContact().equals(contactDetails)).orElse(null);
        if (doc != null) {
            return emailService.sendFoundEmail(email);
        }
        else {
            return ResponseEntity.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message("Try again some time later, doc not found.")
                    .build();
        }
    }

    private String getPrincipal() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Kindly login first");
        }
        return authentication.getName();
    }
}
