package com.finder.LnF.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DocRepository extends JpaRepository<Doc, Integer> {
    Optional<Doc>findByDocumentNo(String documentNo);
    Optional<Doc>findByDocumentTypeAndDocumentNo(String documentType, String documentNo);
}
