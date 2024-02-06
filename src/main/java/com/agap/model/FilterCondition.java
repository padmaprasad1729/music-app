package com.agap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FilterCondition {

    private String field;
    private FilterOperationEnum operator;
    private Object value;

}