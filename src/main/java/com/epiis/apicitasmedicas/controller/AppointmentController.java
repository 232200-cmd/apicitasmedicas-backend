package com.epiis.apicitasmedicas.controller;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "appointment")
public class AppointmentController {
    private final BusinessAppointment businessAppointment;

    public AppointmentController(BusinessAppointment businessAppointment) {
        this.businessAppointment = businessAppointment;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    String cleanText = text.endsWith("Z") ? text.substring(0, text.length() - 1) : text;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    setValue(sdf.parse(cleanText));
                } catch (Exception e1) {
                    try {
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        setValue(sdf2.parse(text));
                    } catch (Exception e2) {
                        throw new IllegalArgumentException("Formato de fecha no válido: " + text);
                    }
                }
            }
        });
    }

    // Cualquier usuario autenticado (Paciente o Administrador) puede crear una solicitud de cita
    @PostMapping(path = "insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAppointmentInsert> actionInsert(@Valid @ModelAttribute RequestAppointmentInsert request, BindingResult bindingResult) {
        try {
            ResponseAppointmentInsert response;
            if (bindingResult.hasErrors()) {
                response = new ResponseAppointmentInsert();
                bindingResult.getAllErrors().forEach(error -> response.listMessage.add(error.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessAppointment.insert(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }

    // El Paciente ve solo las suyas (filtrado en BusinessAppointment), el Admin ve todas
    @GetMapping(path = "getall")
    public ResponseEntity<ResponseAppointmentGetAll> actionGetAll() {
        try {
            return ResponseEntity.ok(businessAppointment.getAll());
        } catch (Exception e) {
            return null;
        }
    }

    // SOLO Administrador puede cambiar estados
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping(path = "seen/{idAppointment}")
    public ResponseEntity<ResponseAppointmentSeen> actionSeen(@PathVariable String idAppointment) {
        try {
            return ResponseEntity.ok(businessAppointment.seen(idAppointment));
        } catch (Exception e) {
            return null;
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping(path = "coordination/{idAppointment}")
    public ResponseEntity<ResponseAppointmentCoordination> actionCoordination(@PathVariable String idAppointment) {
        try {
            return ResponseEntity.ok(businessAppointment.coordination(idAppointment));
        } catch (Exception e) {
            return null;
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping(path = "reject/{idAppointment}")
    public ResponseEntity<ResponseAppointmentReject> actionReject(@PathVariable String idAppointment) {
        try {
            return ResponseEntity.ok(businessAppointment.reject(idAppointment));
        } catch (Exception e) {
            return null;
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping(path = "close/{idAppointment}")
    public ResponseEntity<ResponseAppointmentClose> actionClose(@PathVariable String idAppointment) {
        try {
            return ResponseEntity.ok(businessAppointment.close(idAppointment));
        } catch (Exception e) {
            return null;
        }
    }

    // SOLO Administrador puede comentar (son comentarios internos del staff)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping(path = "comment/insert")
    public ResponseEntity<ResponseAppointmentComment> actionComment(@Valid @RequestBody RequestAppointmentComment request, BindingResult bindingResult) {
        try {
            ResponseAppointmentComment response;
            if (bindingResult.hasErrors()) {
                response = new ResponseAppointmentComment();
                bindingResult.getAllErrors().forEach(error -> response.listMessage.add(error.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessAppointment.comment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }
}