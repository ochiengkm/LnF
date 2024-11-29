package com.finder.LnF.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {
    @Query(value = "SELECT d FROM Doc d WHERE d.documentNo = :documentNo")
    Optional<Doc>findByDocumentNo(@Param("documentNo") String documentNo);

    Optional<Doc>findByDocumentTypeAndDocumentNo(DocType documentType, String documentNo);
}
