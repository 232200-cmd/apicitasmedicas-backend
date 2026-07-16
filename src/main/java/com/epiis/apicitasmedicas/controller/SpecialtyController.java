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
}
