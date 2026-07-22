package com.epiis.apicitasmedicas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apicitasmedicas.business.BusinessDoctor;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetBySpecialty;

@RestController
@RequestMapping(path = "doctor")
public class DoctorController {
    private final BusinessDoctor businessDoctor;

    public DoctorController(BusinessDoctor businessDoctor) {
        this.businessDoctor = businessDoctor;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseDoctorGetAll> actionGetAll() {
        try {
            return ResponseEntity.ok(businessDoctor.getAll());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(path = "getbyspecialty/{idSpecialty}")
    public ResponseEntity<ResponseDoctorGetBySpecialty> actionGetBySpecialty(@PathVariable String idSpecialty) {
        try {
            return ResponseEntity.ok(businessDoctor.getBySpecialty(idSpecialty));
        } catch (Exception e) {
            return null;
        }
    }

    @org.springframework.web.bind.annotation.PostMapping(path = "insert")
    public ResponseEntity<com.epiis.apicitasmedicas.dto.response.ResponseDoctorInsert> actionInsert(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.epiis.apicitasmedicas.dto.request.RequestDoctorInsert request) {
        try {
            return ResponseEntity.ok(businessDoctor.insert(request));
        } catch (Exception e) {
            return null;
        }
    }

    @org.springframework.web.bind.annotation.PutMapping(path = "update")
    public ResponseEntity<com.epiis.apicitasmedicas.dto.response.ResponseDoctorUpdate> actionUpdate(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.epiis.apicitasmedicas.dto.request.RequestDoctorUpdate request) {
        try {
            return ResponseEntity.ok(businessDoctor.update(request));
        } catch (Exception e) {
            return null;
        }
    }

    @org.springframework.web.bind.annotation.DeleteMapping(path = "delete/{idDoctor}")
    public ResponseEntity<com.epiis.apicitasmedicas.dto.response.ResponseDoctorDelete> actionDelete(@org.springframework.web.bind.annotation.PathVariable String idDoctor) {
        try {
            return ResponseEntity.ok(businessDoctor.delete(idDoctor));
        } catch (Exception e) {
            return null;
        }
    }
}