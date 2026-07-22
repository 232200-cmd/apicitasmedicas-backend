package com.epiis.apicitasmedicas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apicitasmedicas.business.BusinessSpecialty;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyGetAll;

@RestController
@RequestMapping(path = "specialty")
public class SpecialtyController {
    private final BusinessSpecialty businessSpecialty;

    public SpecialtyController(BusinessSpecialty businessSpecialty) {
        this.businessSpecialty = businessSpecialty;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseSpecialtyGetAll> actionGetAll() {
        try {
            return ResponseEntity.ok(businessSpecialty.getAll());
        } catch (Exception e) {
            return null;
        }
    }

    @org.springframework.web.bind.annotation.PostMapping(path = "insert")
    public ResponseEntity<com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyInsert> actionInsert(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.epiis.apicitasmedicas.dto.request.RequestSpecialtyInsert request) {
        try {
            return ResponseEntity.ok(businessSpecialty.insert(request));
        } catch (Exception e) {
            return null;
        }
    }

    @org.springframework.web.bind.annotation.PutMapping(path = "update")
    public ResponseEntity<com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyUpdate> actionUpdate(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.epiis.apicitasmedicas.dto.request.RequestSpecialtyUpdate request) {
        try {
            return ResponseEntity.ok(businessSpecialty.update(request));
        } catch (Exception e) {
            return null;
        }
    }

    @org.springframework.web.bind.annotation.DeleteMapping(path = "delete/{idSpecialty}")
    public ResponseEntity<com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyDelete> actionDelete(@org.springframework.web.bind.annotation.PathVariable String idSpecialty) {
        try {
            return ResponseEntity.ok(businessSpecialty.delete(idSpecialty));
        } catch (Exception e) {
            return null;
        }
    }
}
