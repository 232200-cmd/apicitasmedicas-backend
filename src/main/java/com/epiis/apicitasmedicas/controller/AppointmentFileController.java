package com.epiis.apicitasmedicas.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apicitasmedicas.entity.EntityAppointmentFile;
import com.epiis.apicitasmedicas.repository.RepositoryAppointmentFile;

@RestController
@RequestMapping(path = "appointment/file")
public class AppointmentFileController {

    private final RepositoryAppointmentFile repositoryAppointmentFile;

    public AppointmentFileController(RepositoryAppointmentFile repositoryAppointmentFile) {
        this.repositoryAppointmentFile = repositoryAppointmentFile;
    }

    @GetMapping(path = "download/{idAppointmentfile}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String idAppointmentfile) {
        try {
            EntityAppointmentFile entityFile = repositoryAppointmentFile.findById(idAppointmentfile).orElse(null);

            if (entityFile == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get("storage/appointmentfile")
                    .resolve(entityFile.getIdAppointmentfile() + "." + entityFile.getExtension());

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entityFile.getName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}