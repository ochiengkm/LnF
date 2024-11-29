package com.finder.LnF.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finder.LnF.document.Doc;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "location")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "county", nullable = false)
    private String county;

    @Column(name = "sub_county", nullable = false)
    private String subCounty;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "village", nullable = true)
    private String village;

    @Column(name = "nearest_huduma_centre", nullable = false)
    private String nearestHudumaCentre;

    @Column(name = "nearest_gok_facility", nullable = true)
    private String nearestGokFacility;

    @OneToMany(mappedBy = "locationDetails", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doc> docs;
}
