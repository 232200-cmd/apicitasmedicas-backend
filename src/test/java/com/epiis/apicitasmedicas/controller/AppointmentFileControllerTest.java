package com.epiis.apicitasmedicas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.epiis.apicitasmedicas.entity.EntityAppointmentFile;
import com.epiis.apicitasmedicas.repository.RepositoryAppointmentFile;

class AppointmentFileControllerTest {

    @Mock
    private RepositoryAppointmentFile repositoryAppointmentFile;

    @InjectMocks
    private AppointmentFileController appointmentFileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActionDownload_FileNotFoundInDb() {
        when(repositoryAppointmentFile.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<Resource> response = appointmentFileController.downloadFile("1");

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testActionDownload_FileExistsInDbButNotInFileSystem() {
        EntityAppointmentFile fileEntity = new EntityAppointmentFile();
        fileEntity.setIdAppointmentfile("nonexistent-file-id");
        fileEntity.setExtension("pdf");

        when(repositoryAppointmentFile.findById("1")).thenReturn(Optional.of(fileEntity));

        ResponseEntity<Resource> response = appointmentFileController.downloadFile("1");

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }
}
