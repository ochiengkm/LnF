package com.finder.LnF.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocDTO {
    private String documentNo;
    private LocalDate dob;
    private String officialDocumentNames;
    private String description;
}
