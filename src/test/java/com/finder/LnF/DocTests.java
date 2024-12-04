package com.finder.LnF;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finder.LnF.document.Doc;
import com.finder.LnF.document.DocDTO;
import com.finder.LnF.document.DocService;
import com.finder.LnF.document.DocType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static springfox.documentation.builders.PathSelectors.any;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
@Transactional
public class DocTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DocService docService;

    @Test
    void testCreateDoc() throws Exception {
        List<Doc> documents = loadJsonData("src/test/resources/lnf_doc.json");

        for (Doc document : documents) {
            DocType docType = document.getDocumentType();
            String requestBody = objectMapper.writeValueAsString(document);

            mockMvc.perform(MockMvcRequestBuilders.post("/document/capture")
                            .param("docType", String.valueOf(docType))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated());
        }
//        verify(docService, times(documents.size())).captureDoc(ArgumentMatchers.any(DocDTO.class, documents.get(0).getDocumentType()));

    }

    private List<Doc> loadJsonData(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

}
