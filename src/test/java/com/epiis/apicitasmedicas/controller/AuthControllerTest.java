package com.epiis.apicitasmedicas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.epiis.apicitasmedicas.business.BusinessAuth;
import com.epiis.apicitasmedicas.dto.request.RequestAuthLogin;
import com.epiis.apicitasmedicas.dto.request.RequestAuthRegister;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthLogin;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthRegister;

class AuthControllerTest {

    @Mock
    private BusinessAuth businessAuth;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActionLogin_Success() {
        RequestAuthLogin request = new RequestAuthLogin();
        ResponseAuthLogin mockResponse = new ResponseAuthLogin();
        mockResponse.success();

        when(businessAuth.login(any(RequestAuthLogin.class))).thenReturn(mockResponse);

        // Si tu controlador usa BindingResult, mockéalo
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<ResponseAuthLogin> response = authController.actionLogin(request, bindingResult);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testActionLogin_Exception() {
        when(businessAuth.login(any(RequestAuthLogin.class))).thenThrow(new RuntimeException());
        when(bindingResult.hasErrors()).thenReturn(false);
        ResponseEntity<ResponseAuthLogin> response = authController.actionLogin(new RequestAuthLogin(), bindingResult);
        assertNull(response);
    }

    @Test
    void testActionRegister_Success() {
        RequestAuthRegister request = new RequestAuthRegister();
        ResponseAuthRegister mockResponse = new ResponseAuthRegister();
        mockResponse.success();

        when(businessAuth.register(any(RequestAuthRegister.class))).thenReturn(mockResponse);

        ResponseEntity<ResponseAuthRegister> response = authController.actionRegister(request, bindingResult);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testActionRegister_Exception() {
        when(businessAuth.register(any(RequestAuthRegister.class))).thenThrow(new RuntimeException());
        when(bindingResult.hasErrors()).thenReturn(false);
        ResponseEntity<ResponseAuthRegister> response = authController.actionRegister(new RequestAuthRegister(), bindingResult);
        assertNull(response);
    }
}
