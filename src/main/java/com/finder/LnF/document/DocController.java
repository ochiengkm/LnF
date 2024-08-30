package com.finder.LnF.document;

import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@CrossOrigin
@RequiredArgsConstructor
public class DocController {
    private final DocService docService;

    @GetMapping("/find") //Fetch Document by type and Document number
    public ResponseEntity<Doc> findDoc(DocType docType, String documentNo) {
        return docService.findDocument(docType, documentNo);
    }

    @PostMapping("/capture") //Capture Document
    public ResponseEntity<Doc> captureDoc(@RequestBody Doc doc, DocType docType){
        return docService.captureDoc(doc, docType);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Doc> deleteDoc(String documentNo){
        return docService.deleteDocument(documentNo);
    }
}
