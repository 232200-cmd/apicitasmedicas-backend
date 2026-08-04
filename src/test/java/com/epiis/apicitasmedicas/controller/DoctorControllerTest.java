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

import com.epiis.apicitasmedicas.business.BusinessDoctor;
import com.epiis.apicitasmedicas.dto.request.RequestDoctorInsert;
import com.epiis.apicitasmedicas.dto.request.RequestDoctorUpdate;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorDelete;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetBySpecialty;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorUpdate;

class DoctorControllerTest {

    @Mock
    private BusinessDoctor businessDoctor;

    @InjectMocks
    private DoctorController doctorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActionGetAll() {
        ResponseDoctorGetAll mockResponse = new ResponseDoctorGetAll();
        mockResponse.success();
        
        when(businessDoctor.getAll()).thenReturn(mockResponse);

        ResponseEntity<ResponseDoctorGetAll> response = doctorController.actionGetAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }
    
    @Test
    void testActionGetAll_Exception() {
        when(businessDoctor.getAll()).thenThrow(new RuntimeException());
        ResponseEntity<ResponseDoctorGetAll> response = doctorController.actionGetAll();
        assertNull(response);
    }

    @Test
    void testActionGetBySpecialty() {
        ResponseDoctorGetBySpecialty mockResponse = new ResponseDoctorGetBySpecialty();
        mockResponse.success();
        
        when(businessDoctor.getBySpecialty(anyString())).thenReturn(mockResponse);

        ResponseEntity<ResponseDoctorGetBySpecialty> response = doctorController.actionGetBySpecialty("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }
    
    @Test
    void testActionGetBySpecialty_Exception() {
        when(businessDoctor.getBySpecialty(anyString())).thenThrow(new RuntimeException());
        ResponseEntity<ResponseDoctorGetBySpecialty> response = doctorController.actionGetBySpecialty("1");
        assertNull(response);
    }

    @Test
    void testActionInsert() {
        RequestDoctorInsert request = new RequestDoctorInsert();
        ResponseDoctorInsert mockResponse = new ResponseDoctorInsert();
        mockResponse.success();

        when(businessDoctor.insert(any(RequestDoctorInsert.class))).thenReturn(mockResponse);

        ResponseEntity<ResponseDoctorInsert> response = doctorController.actionInsert(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionInsert_Exception() {
        when(businessDoctor.insert(any(RequestDoctorInsert.class))).thenThrow(new RuntimeException());
        ResponseEntity<ResponseDoctorInsert> response = doctorController.actionInsert(new RequestDoctorInsert());
        assertNull(response);
    }

    @Test
    void testActionUpdate() {
        RequestDoctorUpdate request = new RequestDoctorUpdate();
        ResponseDoctorUpdate mockResponse = new ResponseDoctorUpdate();
        mockResponse.success();

        when(businessDoctor.update(any(RequestDoctorUpdate.class))).thenReturn(mockResponse);

        ResponseEntity<ResponseDoctorUpdate> response = doctorController.actionUpdate(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionUpdate_Exception() {
        when(businessDoctor.update(any(RequestDoctorUpdate.class))).thenThrow(new RuntimeException());
        ResponseEntity<ResponseDoctorUpdate> response = doctorController.actionUpdate(new RequestDoctorUpdate());
        assertNull(response);
    }

    @Test
    void testActionDelete() {
        ResponseDoctorDelete mockResponse = new ResponseDoctorDelete();
        mockResponse.success();

        when(businessDoctor.delete(anyString())).thenReturn(mockResponse);

        ResponseEntity<ResponseDoctorDelete> response = doctorController.actionDelete("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionDelete_Exception() {
        when(businessDoctor.delete(anyString())).thenThrow(new RuntimeException());
        ResponseEntity<ResponseDoctorDelete> response = doctorController.actionDelete("1");
        assertNull(response);
    }
}
