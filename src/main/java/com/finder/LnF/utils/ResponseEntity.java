package com.finder.LnF.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseEntity<T> {
    private Integer statusCode;
    private String message;
    private T entity;

    public ResponseEntity(T objectType){
        this.entity = objectType;
    }

}
