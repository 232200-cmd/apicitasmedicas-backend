package com.epiis.apicitasmedicas.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epiis.apicitasmedicas.dto.request.RequestSpecialtyInsert;
import com.epiis.apicitasmedicas.dto.request.RequestSpecialtyUpdate;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyDelete;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyUpdate;
import com.epiis.apicitasmedicas.entity.EntitySpecialty;
import com.epiis.apicitasmedicas.repository.RepositorySpecialty;

class BusinessSpecialtyTest {

    @Mock
    private RepositorySpecialty repositorySpecialty;

    @InjectMocks
    private BusinessSpecialty businessSpecialty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<EntitySpecialty> mockList = new ArrayList<>();
        EntitySpecialty specialty = new EntitySpecialty();
        specialty.setIdSpecialty("1");
        specialty.setName("Cardiología");
        mockList.add(specialty);

        when(repositorySpecialty.findAll()).thenReturn(mockList);

        ResponseSpecialtyGetAll response = businessSpecialty.getAll();

        assertEquals("success", response.getType());
        assertEquals(1, response.getListSpecialty().size());
        assertEquals("Cardiología", response.getListSpecialty().get(0).get("name"));
    }

    @Test
    void testInsert_Success() {
        RequestSpecialtyInsert request = new RequestSpecialtyInsert();
        request.setName("Neurología");

        when(repositorySpecialty.existsByNameIgnoreCase("Neurología")).thenReturn(false);
        when(repositorySpecialty.save(any(EntitySpecialty.class))).thenReturn(new EntitySpecialty());

        ResponseSpecialtyInsert response = businessSpecialty.insert(request);

        assertEquals("success", response.getType());
        assertEquals("Especialidad insertada correctamente.", response.listMessage.get(0));
    }

    @Test
    void testInsert_AlreadyExists() {
        RequestSpecialtyInsert request = new RequestSpecialtyInsert();
        request.setName("Neurología");

        when(repositorySpecialty.existsByNameIgnoreCase("Neurología")).thenReturn(true);

        ResponseSpecialtyInsert response = businessSpecialty.insert(request);

        assertEquals("error", response.getType());
        assertEquals("Ya existe una especialidad con este nombre.", response.listMessage.get(0));
    }

    @Test
    void testUpdate_Success() {
        RequestSpecialtyUpdate request = new RequestSpecialtyUpdate();
        request.setIdSpecialty("1");
        request.setName("Pediatría");

        EntitySpecialty existingSpecialty = new EntitySpecialty();
        existingSpecialty.setIdSpecialty("1");

        when(repositorySpecialty.findById("1")).thenReturn(Optional.of(existingSpecialty));
        when(repositorySpecialty.existsByNameIgnoreCaseAndIdSpecialtyNot("Pediatría", "1")).thenReturn(false);

        ResponseSpecialtyUpdate response = businessSpecialty.update(request);

        assertEquals("success", response.getType());
        assertEquals("Especialidad actualizada correctamente.", response.listMessage.get(0));
    }

    @Test
    void testUpdate_NotFound() {
        RequestSpecialtyUpdate request = new RequestSpecialtyUpdate();
        request.setIdSpecialty("1");

        when(repositorySpecialty.findById("1")).thenReturn(Optional.empty());

        ResponseSpecialtyUpdate response = businessSpecialty.update(request);

        assertEquals("error", response.getType());
        assertEquals("La especialidad no existe.", response.listMessage.get(0));
    }
    
    @Test
    void testUpdate_AlreadyExists() {
        RequestSpecialtyUpdate request = new RequestSpecialtyUpdate();
        request.setIdSpecialty("1");
        request.setName("Pediatría");

        EntitySpecialty existingSpecialty = new EntitySpecialty();
        existingSpecialty.setIdSpecialty("1");

        when(repositorySpecialty.findById("1")).thenReturn(Optional.of(existingSpecialty));
        when(repositorySpecialty.existsByNameIgnoreCaseAndIdSpecialtyNot("Pediatría", "1")).thenReturn(true);

        ResponseSpecialtyUpdate response = businessSpecialty.update(request);

        assertEquals("error", response.getType());
        assertEquals("Ya existe otra especialidad con este nombre.", response.listMessage.get(0));
    }

    @Test
    void testDelete_Success() {
        when(repositorySpecialty.existsById("1")).thenReturn(true);
        doNothing().when(repositorySpecialty).deleteById("1");

        ResponseSpecialtyDelete response = businessSpecialty.delete("1");

        assertEquals("success", response.getType());
        assertEquals("Especialidad eliminada correctamente.", response.listMessage.get(0));
    }

    @Test
    void testDelete_NotFound() {
        when(repositorySpecialty.existsById("1")).thenReturn(false);

        ResponseSpecialtyDelete response = businessSpecialty.delete("1");

        assertEquals("error", response.getType());
        assertEquals("La especialidad no existe.", response.listMessage.get(0));
    }
    
    @Test
    void testDelete_Exception() {
        when(repositorySpecialty.existsById("1")).thenReturn(true);
        doThrow(new RuntimeException()).when(repositorySpecialty).deleteById("1");

        ResponseSpecialtyDelete response = businessSpecialty.delete("1");

        assertEquals("error", response.getType());
        assertEquals("No se puede eliminar la especialidad porque está siendo utilizada.", response.listMessage.get(0));
    }
}
