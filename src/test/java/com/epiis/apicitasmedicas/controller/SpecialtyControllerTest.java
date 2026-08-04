package com.epiis.apicitasmedicas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.epiis.apicitasmedicas.business.BusinessSpecialty;
import com.epiis.apicitasmedicas.dto.request.RequestSpecialtyInsert;
import com.epiis.apicitasmedicas.dto.request.RequestSpecialtyUpdate;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyDelete;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyUpdate;

class SpecialtyControllerTest {

    @Mock
    private BusinessSpecialty businessSpecialty;

    @InjectMocks
    private SpecialtyController specialtyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActionGetAll() {
        ResponseSpecialtyGetAll mockResponse = new ResponseSpecialtyGetAll();
        mockResponse.success();
        
        when(businessSpecialty.getAll()).thenReturn(mockResponse);

        ResponseEntity<ResponseSpecialtyGetAll> response = specialtyController.actionGetAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }
    
    @Test
    void testActionGetAll_Exception() {
        when(businessSpecialty.getAll()).thenThrow(new RuntimeException());
        ResponseEntity<ResponseSpecialtyGetAll> response = specialtyController.actionGetAll();
        assertNull(response);
    }

    @Test
    void testActionInsert() {
        RequestSpecialtyInsert request = new RequestSpecialtyInsert();
        ResponseSpecialtyInsert mockResponse = new ResponseSpecialtyInsert();
        mockResponse.success();

        when(businessSpecialty.insert(any(RequestSpecialtyInsert.class))).thenReturn(mockResponse);

        ResponseEntity<ResponseSpecialtyInsert> response = specialtyController.actionInsert(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionInsert_Exception() {
        when(businessSpecialty.insert(any(RequestSpecialtyInsert.class))).thenThrow(new RuntimeException());
        ResponseEntity<ResponseSpecialtyInsert> response = specialtyController.actionInsert(new RequestSpecialtyInsert());
        assertNull(response);
    }

    @Test
    void testActionUpdate() {
        RequestSpecialtyUpdate request = new RequestSpecialtyUpdate();
        ResponseSpecialtyUpdate mockResponse = new ResponseSpecialtyUpdate();
        mockResponse.success();

        when(businessSpecialty.update(any(RequestSpecialtyUpdate.class))).thenReturn(mockResponse);

        ResponseEntity<ResponseSpecialtyUpdate> response = specialtyController.actionUpdate(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }
    
    @Test
    void testActionUpdate_Exception() {
        when(businessSpecialty.update(any(RequestSpecialtyUpdate.class))).thenThrow(new RuntimeException());
        ResponseEntity<ResponseSpecialtyUpdate> response = specialtyController.actionUpdate(new RequestSpecialtyUpdate());
        assertNull(response);
    }

    @Test
    void testActionDelete() {
        ResponseSpecialtyDelete mockResponse = new ResponseSpecialtyDelete();
        mockResponse.success();

        when(businessSpecialty.delete(anyString())).thenReturn(mockResponse);

        ResponseEntity<ResponseSpecialtyDelete> response = specialtyController.actionDelete("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }
    
    @Test
    void testActionDelete_Exception() {
        when(businessSpecialty.delete(anyString())).thenThrow(new RuntimeException());
        ResponseEntity<ResponseSpecialtyDelete> response = specialtyController.actionDelete("1");
        assertNull(response);
    }
}
