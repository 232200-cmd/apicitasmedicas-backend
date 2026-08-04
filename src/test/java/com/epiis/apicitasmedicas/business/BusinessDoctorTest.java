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

import com.epiis.apicitasmedicas.dto.request.RequestDoctorInsert;
import com.epiis.apicitasmedicas.dto.request.RequestDoctorUpdate;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorDelete;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetBySpecialty;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorUpdate;
import com.epiis.apicitasmedicas.entity.EntityDoctor;
import com.epiis.apicitasmedicas.entity.EntityDoctorSpecialty;
import com.epiis.apicitasmedicas.entity.EntitySpecialty;
import com.epiis.apicitasmedicas.repository.RepositoryDoctor;
import com.epiis.apicitasmedicas.repository.RepositoryDoctorSpecialty;

class BusinessDoctorTest {

    @Mock
    private RepositoryDoctor repositoryDoctor;

    @Mock
    private RepositoryDoctorSpecialty repositoryDoctorSpecialty;

    @InjectMocks
    private BusinessDoctor businessDoctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<EntityDoctor> mockList = new ArrayList<>();
        EntityDoctor doctor = new EntityDoctor();
        doctor.setIdDoctor("1");
        doctor.setFirstName("Juan");
        doctor.setSurName("Perez");
        mockList.add(doctor);

        EntityDoctorSpecialty relation = new EntityDoctorSpecialty();
        relation.setIdSpecialty("10");
        EntitySpecialty specialty = new EntitySpecialty();
        specialty.setName("Cardiologia");
        relation.setParentSpecialty(specialty);
        
        when(repositoryDoctor.findAll()).thenReturn(mockList);
        when(repositoryDoctorSpecialty.findByIdDoctor("1")).thenReturn(List.of(relation));

        ResponseDoctorGetAll response = businessDoctor.getAll();

        assertEquals("success", response.getType());
        assertEquals(1, response.getListDoctor().size());
        assertEquals("Juan", response.getListDoctor().get(0).get("firstName"));
        assertEquals("Cardiologia", response.getListDoctor().get(0).get("specialtyName"));
    }

    @Test
    void testGetBySpecialty() {
        EntityDoctorSpecialty relation = new EntityDoctorSpecialty();
        EntityDoctor doctor = new EntityDoctor();
        doctor.setIdDoctor("1");
        doctor.setFirstName("Juan");
        doctor.setSurName("Perez");
        relation.setParentDoctor(doctor);
        
        when(repositoryDoctorSpecialty.findByIdSpecialty("10")).thenReturn(List.of(relation));

        ResponseDoctorGetBySpecialty response = businessDoctor.getBySpecialty("10");

        assertEquals("success", response.getType());
        assertEquals(1, response.getListDoctor().size());
        assertEquals("Juan", response.getListDoctor().get(0).get("firstName"));
    }

    @Test
    void testInsert_Success() {
        RequestDoctorInsert request = new RequestDoctorInsert();
        request.setFirstName("Ana");
        request.setEmail("ana@gmail.com");
        request.setPhoneNumber("123456789");
        request.setIdSpecialty("10");

        when(repositoryDoctor.existsByEmail("ana@gmail.com")).thenReturn(false);
        when(repositoryDoctor.existsByPhoneNumber("123456789")).thenReturn(false);
        when(repositoryDoctor.save(any(EntityDoctor.class))).thenReturn(new EntityDoctor());
        when(repositoryDoctorSpecialty.save(any(EntityDoctorSpecialty.class))).thenReturn(new EntityDoctorSpecialty());

        ResponseDoctorInsert response = businessDoctor.insert(request);

        assertEquals("success", response.getType());
        assertEquals("Doctor insertado correctamente.", response.listMessage.get(0));
    }

    @Test
    void testInsert_EmailExists() {
        RequestDoctorInsert request = new RequestDoctorInsert();
        request.setEmail("ana@gmail.com");

        when(repositoryDoctor.existsByEmail("ana@gmail.com")).thenReturn(true);

        ResponseDoctorInsert response = businessDoctor.insert(request);

        assertEquals("error", response.getType());
        assertEquals("El email ya está registrado.", response.listMessage.get(0));
    }

    @Test
    void testInsert_PhoneExists() {
        RequestDoctorInsert request = new RequestDoctorInsert();
        request.setEmail("ana@gmail.com");
        request.setPhoneNumber("123456789");

        when(repositoryDoctor.existsByEmail("ana@gmail.com")).thenReturn(false);
        when(repositoryDoctor.existsByPhoneNumber("123456789")).thenReturn(true);

        ResponseDoctorInsert response = businessDoctor.insert(request);

        assertEquals("error", response.getType());
        assertEquals("El teléfono ya está registrado por otro doctor.", response.listMessage.get(0));
    }

    @Test
    void testUpdate_Success() {
        RequestDoctorUpdate request = new RequestDoctorUpdate();
        request.setIdDoctor("1");
        request.setFirstName("Ana");
        request.setEmail("ana@gmail.com");
        request.setPhoneNumber("123456789");
        request.setIdSpecialty("11");

        EntityDoctor existingDoctor = new EntityDoctor();
        existingDoctor.setIdDoctor("1");
        
        EntityDoctorSpecialty relation = new EntityDoctorSpecialty();
        relation.setIdSpecialty("10");

        when(repositoryDoctor.findById("1")).thenReturn(Optional.of(existingDoctor));
        when(repositoryDoctor.existsByEmailAndIdDoctorNot("ana@gmail.com", "1")).thenReturn(false);
        when(repositoryDoctor.existsByPhoneNumberAndIdDoctorNot("123456789", "1")).thenReturn(false);
        when(repositoryDoctorSpecialty.findByIdDoctor("1")).thenReturn(List.of(relation));

        ResponseDoctorUpdate response = businessDoctor.update(request);

        assertEquals("success", response.getType());
        assertEquals("Doctor actualizado correctamente.", response.listMessage.get(0));
        assertEquals("11", relation.getIdSpecialty()); // Updated specialty
    }

    @Test
    void testUpdate_DoctorNotFound() {
        RequestDoctorUpdate request = new RequestDoctorUpdate();
        request.setIdDoctor("1");

        when(repositoryDoctor.findById("1")).thenReturn(Optional.empty());

        ResponseDoctorUpdate response = businessDoctor.update(request);

        assertEquals("error", response.getType());
        assertEquals("El doctor no existe.", response.listMessage.get(0));
    }

    @Test
    void testUpdate_EmailExists() {
        RequestDoctorUpdate request = new RequestDoctorUpdate();
        request.setIdDoctor("1");
        request.setEmail("ana@gmail.com");

        when(repositoryDoctor.findById("1")).thenReturn(Optional.of(new EntityDoctor()));
        when(repositoryDoctor.existsByEmailAndIdDoctorNot("ana@gmail.com", "1")).thenReturn(true);

        ResponseDoctorUpdate response = businessDoctor.update(request);

        assertEquals("error", response.getType());
        assertEquals("El email ya está registrado por otro doctor.", response.listMessage.get(0));
    }

    @Test
    void testUpdate_PhoneExists() {
        RequestDoctorUpdate request = new RequestDoctorUpdate();
        request.setIdDoctor("1");
        request.setEmail("ana@gmail.com");
        request.setPhoneNumber("123456789");

        when(repositoryDoctor.findById("1")).thenReturn(Optional.of(new EntityDoctor()));
        when(repositoryDoctor.existsByEmailAndIdDoctorNot("ana@gmail.com", "1")).thenReturn(false);
        when(repositoryDoctor.existsByPhoneNumberAndIdDoctorNot("123456789", "1")).thenReturn(true);

        ResponseDoctorUpdate response = businessDoctor.update(request);

        assertEquals("error", response.getType());
        assertEquals("El teléfono ya está registrado por otro doctor.", response.listMessage.get(0));
    }

    @Test
    void testDelete_Success() {
        when(repositoryDoctor.existsById("1")).thenReturn(true);
        when(repositoryDoctorSpecialty.findByIdDoctor("1")).thenReturn(new ArrayList<>());
        doNothing().when(repositoryDoctorSpecialty).deleteAll(any());
        doNothing().when(repositoryDoctor).deleteById("1");

        ResponseDoctorDelete response = businessDoctor.delete("1");

        assertEquals("success", response.getType());
        assertEquals("Doctor eliminado correctamente.", response.listMessage.get(0));
    }

    @Test
    void testDelete_NotFound() {
        when(repositoryDoctor.existsById("1")).thenReturn(false);

        ResponseDoctorDelete response = businessDoctor.delete("1");

        assertEquals("error", response.getType());
        assertEquals("El doctor no existe.", response.listMessage.get(0));
    }

    @Test
    void testDelete_Exception() {
        when(repositoryDoctor.existsById("1")).thenReturn(true);
        doThrow(new RuntimeException()).when(repositoryDoctor).deleteById("1");

        ResponseDoctorDelete response = businessDoctor.delete("1");

        assertEquals("error", response.getType());
        assertEquals("No se puede eliminar el doctor porque está siendo utilizado.", response.listMessage.get(0));
    }
}
