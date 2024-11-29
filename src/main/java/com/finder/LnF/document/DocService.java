package com.finder.LnF.document;

import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocService {
    private final DocRepository docRepository;

    public ResponseEntity<Doc> captureDoc(DocDTO docRequest, DocType docType) {
        ResponseEntity<Doc> response = new ResponseEntity<>();
        try {
            if (docRepository.findByDocumentNo(docRequest.getDocumentNo()).isEmpty()) {
                Doc doc =  Doc.builder()
                        .documentType(docType)
                        .documentNo(docRequest.getDocumentNo())
                        .dob(docRequest.getDob())
                        .officialDocumentNames(docRequest.getOfficialDocumentNames())
                        .description(docRequest.getDescription())
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
}
