package com.epiis.apicitasmedicas.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apicitasmedicas.business.BusinessAuth;
import com.epiis.apicitasmedicas.dto.request.RequestAuthLogin;
import com.epiis.apicitasmedicas.dto.request.RequestAuthRegister;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthLogin;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthRegister;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "auth")
public class AuthController {
    private final BusinessAuth businessAuth;

    public AuthController(BusinessAuth businessAuth) {
        this.businessAuth = businessAuth;
    }

    @PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseAuthLogin> actionLogin(@Valid @RequestBody RequestAuthLogin request, BindingResult bindingResult) {
        try {
            ResponseAuthLogin response;
            if (bindingResult.hasErrors()) {
                response = new ResponseAuthLogin();
                bindingResult.getAllErrors().forEach(error -> response.listMessage.add(error.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessAuth.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseAuthRegister> actionRegister(@Valid @RequestBody RequestAuthRegister request, BindingResult bindingResult) {
        try {
            ResponseAuthRegister response;
            if (bindingResult.hasErrors()) {
                response = new ResponseAuthRegister();
                bindingResult.getAllErrors().forEach(error -> response.listMessage.add(error.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessAuth.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }
}
