package com.agap.service;

import com.agap.model.FilterCondition;
import com.agap.model.FilterOperationEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenericFilterCriteriaBuilderTest {

    private GenericFilterCriteriaBuilder filterCriteriaBuilder;

    @BeforeEach
    void setUp() {
        filterCriteriaBuilder = new GenericFilterCriteriaBuilder();
    }

    @Test
    void addCondition_AndOr() {
        List<FilterCondition> andConditions = Arrays.asList(
                new FilterCondition("field1", FilterOperationEnum.EQUAL, "value1"),
                new FilterCondition("field2", FilterOperationEnum.GREATER_THAN, 123)
        );

        List<FilterCondition> orConditions = Arrays.asList(
                new FilterCondition("field3", FilterOperationEnum.NOT_EQUAL, "value3"),
                new FilterCondition("field4", FilterOperationEnum.CONTAINS, "value4")
        );

        Query result = filterCriteriaBuilder.addCondition(andConditions, orConditions);

        assertNotNull(result);

        assertTrue(result.toString().contains("{ \"field1\" : \"value1\"}"));
        assertTrue(result.toString().contains("{ \"field2\" : { \"$gt\" : 123}}"));
        assertTrue(result.toString().contains("{ \"field3\" : { \"$ne\" : \"value3\"}}"));
        assertTrue(result.toString().contains("{ \"field4\" : { \"$regularExpression\" : { \"pattern\" : \"value4\", \"options\" : \"\"}}}"));
        assertTrue(result.toString().contains("$and"));
        assertTrue(result.toString().contains("$or"));
    }

    @Test
    void addCondition_OnlyAnd() {
        List<FilterCondition> andConditions = Arrays.asList(
                new FilterCondition("field1", FilterOperationEnum.EQUAL, "value1"),
                new FilterCondition("field2", FilterOperationEnum.GREATER_THAN, 123)
        );

        Query result = filterCriteriaBuilder.addCondition(andConditions, null);

        assertNotNull(result);

        assertTrue(result.toString().contains("{ \"field1\" : \"value1\"}"));
        assertTrue(result.toString().contains("{ \"field2\" : { \"$gt\" : 123}}"));
        assertTrue(result.toString().contains("$and"));
        assertFalse(result.toString().contains("$or"));
    }

    @Test
    void addCondition_OnlyOr() {
        List<FilterCondition> orConditions = Arrays.asList(
                new FilterCondition("field3", FilterOperationEnum.NOT_EQUAL, "value3"),
                new FilterCondition("field4", FilterOperationEnum.CONTAINS, "value4")
        );

        Query result = filterCriteriaBuilder.addCondition(null, orConditions);

        assertNotNull(result);

        assertTrue(result.toString().contains("{ \"field3\" : { \"$ne\" : \"value3\"}}"));
        assertTrue(result.toString().contains("{ \"field4\" : { \"$regularExpression\" : { \"pattern\" : \"value4\", \"options\" : \"\"}}}"));
        assertFalse(result.toString().contains("$and"));
        assertTrue(result.toString().contains("$or"));
    }

    @Test
    void addCondition_NoAndOr() {
        Query result = filterCriteriaBuilder.addCondition(null, null);

        assertNotNull(result);

        assertFalse(result.toString().contains("$and"));
        assertFalse(result.toString().contains("$or"));
    }

    @Test
    void buildCriteria() {
        FilterCondition condition = new FilterCondition("field1", FilterOperationEnum.EQUAL, "value1");
        Criteria result = filterCriteriaBuilder.buildCriteria(condition);

        assertNotNull(result);
        assertTrue(result.getCriteriaObject().toString().contains("field1=value1"));
    }

    @Test
    void buildCriteria_InvalidFilterOperationEnum() {
        FilterCondition condition = new FilterCondition("field1", null, "value1");
        assertThrows(IllegalArgumentException.class, () -> filterCriteriaBuilder.buildCriteria(condition));
    }
}