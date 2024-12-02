package com.finder.LnF.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDTO {
    private String fullName;
    private String phoneNumber;
    private String email;
    private ContactFlag contactFlag;
}
