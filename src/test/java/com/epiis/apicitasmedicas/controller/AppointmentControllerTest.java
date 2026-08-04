package com.epiis.apicitasmedicas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.epiis.apicitasmedicas.business.BusinessAppointment;
import com.epiis.apicitasmedicas.dto.request.RequestAppointmentComment;
import com.epiis.apicitasmedicas.dto.request.RequestAppointmentInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentClose;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentComment;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentCoordination;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentReject;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentSeen;

class AppointmentControllerTest {

    @Mock
    private BusinessAppointment businessAppointment;
    
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActionInsert() {
        RequestAppointmentInsert request = new RequestAppointmentInsert();
        ResponseAppointmentInsert mockResponse = new ResponseAppointmentInsert();
        mockResponse.success();

        try {
            when(businessAppointment.insert(any(RequestAppointmentInsert.class))).thenReturn(mockResponse);
        } catch (Exception e) {
            /* Mock setup requires catching exception */
        }
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<ResponseAppointmentInsert> response = appointmentController.actionInsert(request, bindingResult);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionInsert_Exception() {
        try {
            when(businessAppointment.insert(any(RequestAppointmentInsert.class))).thenThrow(new RuntimeException());
        } catch (Exception e) {
            /* Mock setup requires catching exception */
        }
        when(bindingResult.hasErrors()).thenReturn(false);
        ResponseEntity<ResponseAppointmentInsert> response = appointmentController.actionInsert(new RequestAppointmentInsert(), bindingResult);
        assertNull(response);
    }

    @Test
    void testActionInsert_BindingErrors() {
        RequestAppointmentInsert request = new RequestAppointmentInsert();
        when(bindingResult.hasErrors()).thenReturn(true);
        java.util.List<org.springframework.validation.ObjectError> errors = java.util.List.of(
            new org.springframework.validation.ObjectError("request", "Error")
        );
        when(bindingResult.getAllErrors()).thenReturn(errors);

        ResponseEntity<ResponseAppointmentInsert> response = appointmentController.actionInsert(request, bindingResult);

        assertNotNull(response);
        assertEquals("Error", response.getBody().listMessage.get(0));
    }

    @Test
    void testActionGetAll() {
        ResponseAppointmentGetAll mockResponse = new ResponseAppointmentGetAll();
        mockResponse.success();

        when(businessAppointment.getAll()).thenReturn(mockResponse);

        ResponseEntity<ResponseAppointmentGetAll> response = appointmentController.actionGetAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionGetAll_Exception() {
        when(businessAppointment.getAll()).thenThrow(new RuntimeException());
        ResponseEntity<ResponseAppointmentGetAll> response = appointmentController.actionGetAll();
        assertNull(response);
    }

    @Test
    void testActionSeen() {
        ResponseAppointmentSeen mockResponse = new ResponseAppointmentSeen();
        mockResponse.success();

        when(businessAppointment.seen(anyString())).thenReturn(mockResponse);

        ResponseEntity<ResponseAppointmentSeen> response = appointmentController.actionSeen("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionSeen_Exception() {
        when(businessAppointment.seen(anyString())).thenThrow(new RuntimeException());
        ResponseEntity<ResponseAppointmentSeen> response = appointmentController.actionSeen("1");
        assertNull(response);
    }

    @Test
    void testActionCoordination() {
        ResponseAppointmentCoordination mockResponse = new ResponseAppointmentCoordination();
        mockResponse.success();

        when(businessAppointment.coordination(anyString())).thenReturn(mockResponse);

        ResponseEntity<ResponseAppointmentCoordination> response = appointmentController.actionCoordination("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionCoordination_Exception() {
        when(businessAppointment.coordination(anyString())).thenThrow(new RuntimeException());
        ResponseEntity<ResponseAppointmentCoordination> response = appointmentController.actionCoordination("1");
        assertNull(response);
    }

    @Test
    void testActionReject() {
        ResponseAppointmentReject mockResponse = new ResponseAppointmentReject();
        mockResponse.success();

        when(businessAppointment.reject(anyString())).thenReturn(mockResponse);

        ResponseEntity<ResponseAppointmentReject> response = appointmentController.actionReject("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionReject_Exception() {
        when(businessAppointment.reject(anyString())).thenThrow(new RuntimeException());
        ResponseEntity<ResponseAppointmentReject> response = appointmentController.actionReject("1");
        assertNull(response);
    }

    @Test
    void testActionClose() {
        ResponseAppointmentClose mockResponse = new ResponseAppointmentClose();
        mockResponse.success();

        when(businessAppointment.close(anyString())).thenReturn(mockResponse);

        ResponseEntity<ResponseAppointmentClose> response = appointmentController.actionClose("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionClose_Exception() {
        when(businessAppointment.close(anyString())).thenThrow(new RuntimeException());
        ResponseEntity<ResponseAppointmentClose> response = appointmentController.actionClose("1");
        assertNull(response);
    }

    @Test
    void testActionComment() {
        RequestAppointmentComment request = new RequestAppointmentComment();
        ResponseAppointmentComment mockResponse = new ResponseAppointmentComment();
        mockResponse.success();

        when(businessAppointment.comment(any(RequestAppointmentComment.class))).thenReturn(mockResponse);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<ResponseAppointmentComment> response = appointmentController.actionComment(request, bindingResult);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("success", response.getBody().getType());
    }

    @Test
    void testActionComment_Exception() {
        when(businessAppointment.comment(any(RequestAppointmentComment.class))).thenThrow(new RuntimeException());
        when(bindingResult.hasErrors()).thenReturn(false);
        ResponseEntity<ResponseAppointmentComment> response = appointmentController.actionComment(new RequestAppointmentComment(), bindingResult);
        assertNull(response);
    }

    @Test
    void testActionComment_BindingErrors() {
        RequestAppointmentComment request = new RequestAppointmentComment();
        when(bindingResult.hasErrors()).thenReturn(true);
        java.util.List<org.springframework.validation.ObjectError> errors = java.util.List.of(
            new org.springframework.validation.ObjectError("request", "Error de validación")
        );
        when(bindingResult.getAllErrors()).thenReturn(errors);

        ResponseEntity<ResponseAppointmentComment> response = appointmentController.actionComment(request, bindingResult);

        assertNotNull(response);
        assertEquals("Error de validación", response.getBody().listMessage.get(0));
    }

    @Test
    void testInitBinder() {
        org.springframework.web.bind.WebDataBinder binder = mock(org.springframework.web.bind.WebDataBinder.class);
        appointmentController.initBinder(binder);
        
        org.mockito.ArgumentCaptor<java.beans.PropertyEditor> captor = org.mockito.ArgumentCaptor.forClass(java.beans.PropertyEditor.class);
        verify(binder).registerCustomEditor(org.mockito.ArgumentMatchers.eq(java.util.Date.class), captor.capture());
        
        java.beans.PropertyEditor editor = captor.getValue();
        
        // Test valid full date string
        editor.setAsText("2026-08-04T12:00:00.000Z");
        assertNotNull(editor.getValue());
        
        // Test valid short date string
        editor.setAsText("2026-08-04");
        assertNotNull(editor.getValue());
        
        // Test invalid date string
        assertThrows(IllegalArgumentException.class, () -> editor.setAsText("invalid-date"));
    }
}
