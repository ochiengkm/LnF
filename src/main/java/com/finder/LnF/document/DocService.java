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
    public ResponseEntity<Doc> captureDoc(Doc doc, DocType docType) {
        ResponseEntity<Doc> response = new ResponseEntity<>();
        try {
            if (docRepository.findByDocumentNo(doc.getDocumentNo()).isEmpty()) {
                doc.setDocumentNo(doc.getDocumentNo());
                doc.setDOB(doc.getDOB());
                doc.setDocumentType(String.valueOf(docType));
                doc.setDescription(doc.getDescription());

                response.setEntity(docRepository.save(doc));
                response.setStatusCode(201);
                response.setMessage("Successfully captured document " + doc.getDocumentNo());
            } else{
                log.warn("Document already exists");
                response.setMessage("Document already exists");
                response.setStatusCode(500);
                response.setEntity(null);
            }
        } catch (Exception e) {
            log.error("Error capturing Doc", e);
        }
        return response;
    }

    public ResponseEntity<Doc> findDocument(DocType docType, String documentNo) {
        ResponseEntity<Doc> response = new ResponseEntity<>();
        Optional<Doc> doc = docRepository.findByDocumentTypeAndDocumentNo(docType.toString(), documentNo);
        try {
            if (doc.isPresent()) {
                response.setMessage("Document found");
                response.setStatusCode(200);
                response.setEntity(doc.get());
            }
            else {
                response.setMessage("Document not found");
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
