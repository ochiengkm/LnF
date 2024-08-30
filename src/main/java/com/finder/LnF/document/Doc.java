package com.finder.LnF.document;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "documents")
public class Doc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String documentType;
    private String documentNo;
    private int DOB;
    private String description;
//    private Boolean isDeleted = false;
}
