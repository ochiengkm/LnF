package com.finder.LnF.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finder.LnF.contact.Contact;
import com.finder.LnF.location.LocationDetails;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doc")
public class Doc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocType documentType;

    @Column(name = "document_no", nullable = false)
    private String documentNo;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "official_document_names", nullable = false)
    private String officialDocumentNames;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = true)
    private LocationDetails locationDetails;

    @OneToOne(mappedBy = "doc", cascade = CascadeType.ALL, orphanRemoval = true)
    private Contact contact;

}
