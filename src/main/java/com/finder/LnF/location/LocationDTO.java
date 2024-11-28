package com.finder.LnF.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private String county;
    private String subCounty;
    private String location;
    private String village;
    private String nearestHudumaCentre;
    private String nearestGokFacility;
}
