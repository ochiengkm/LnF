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

    @GetMapping("/find")
    public ResponseEntity<Doc> findDoc(@RequestParam DocType docType, @RequestParam String documentNo) {
        return docService.findDocument(docType, documentNo);
    }

    @PostMapping("/capture")
    public ResponseEntity<Doc> captureDoc(@RequestBody DocDTO doc, @RequestParam DocType docType){
        return docService.captureDoc(doc, docType);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Doc> deleteDoc(@RequestParam String documentNo){
        return docService.deleteDocument(documentNo);
    }

//    @PatchMapping("/update-details")
//    public ResponseEntity<Doc> updateDoc(@RequestBody DocDTO doc, @RequestParam String documentNo){
//        return docService.updateDocument(doc, documentNo);
//    }
}
