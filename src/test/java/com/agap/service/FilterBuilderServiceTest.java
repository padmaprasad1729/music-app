package com.agap.service;

import com.agap.exception.BadRequestException;
import com.agap.model.FilterCondition;
import com.agap.model.FilterOperationEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FilterBuilderServiceTest {

    private FilterBuilderService filterBuilderService;

    @BeforeEach
    void setUp() {
        filterBuilderService = new FilterBuilderService();
    }

    @Test
    void createFilterCondition() {
        String criteria = "field1|eq|value1&field2|gt|123";
        List<FilterCondition> result = filterBuilderService.createFilterCondition(criteria);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("field1", result.get(0).getField());
        assertEquals(FilterOperationEnum.EQUAL, result.get(0).getOperator());
        assertEquals("value1", result.get(0).getValue());

        assertEquals("field2", result.get(1).getField());
        assertEquals(FilterOperationEnum.GREATER_THAN, result.get(1).getOperator());
        assertEquals(123L, result.get(1).getValue());
    }

    @Test
    void createFilterCondition_Exception() {
        String invalidCriteria = "invalidCriteria";
        assertThrows(BadRequestException.class, () -> filterBuilderService.createFilterCondition(invalidCriteria));
    }

    @Test
    void getPageable() {
        int size = 10;
        int page = 2;
        String order = "fieldName|ASC";

        assertNotNull(filterBuilderService.getPageable(size, page, order));
    }

    @Test
    void getPageable_NoOrder() {
        int size = 10;
        int page = 2;
        String order = "year|asc";

        assertNotNull(filterBuilderService.getPageable(size, page, order));
    }

    @Test
    void getPageable_InvalidOrder() {
        int size = 10;
        int page = 2;
        String invalidOrder = "fieldName|INVALID";

        assertThrows(BadRequestException.class, () -> filterBuilderService.getPageable(size, page, invalidOrder));
    }

    @Test
    void getPageable_Exception() {
        int size = 10;
        int page = 0;
        String order = "null";

        assertThrows(BadRequestException.class, () -> filterBuilderService.getPageable(size, page, order));
    }
}