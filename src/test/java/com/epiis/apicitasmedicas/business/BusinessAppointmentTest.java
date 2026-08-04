package com.epiis.apicitasmedicas.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import com.epiis.apicitasmedicas.dto.request.RequestAppointmentComment;
import com.epiis.apicitasmedicas.dto.request.RequestAppointmentInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentClose;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentComment;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentCoordination;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentInsert;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentReject;
import com.epiis.apicitasmedicas.dto.response.ResponseAppointmentSeen;
import com.epiis.apicitasmedicas.entity.EntityAppointment;
import com.epiis.apicitasmedicas.entity.EntityAppointmentComment;
import com.epiis.apicitasmedicas.entity.EntityAppointmentFile;
import com.epiis.apicitasmedicas.entity.EntityDoctor;
import com.epiis.apicitasmedicas.entity.EntitySpecialty;
import com.epiis.apicitasmedicas.repository.RepositoryAppointment;
import com.epiis.apicitasmedicas.repository.RepositoryAppointmentComment;
import com.epiis.apicitasmedicas.repository.RepositoryAppointmentFile;
import com.epiis.apicitasmedicas.security.SecurityUtil;

class BusinessAppointmentTest {

    @Mock
    private RepositoryAppointment repositoryAppointment;

    @Mock
    private RepositoryAppointmentFile repositoryAppointmentFile;

    @Mock
    private RepositoryAppointmentComment repositoryAppointmentComment;

    @InjectMocks
    private BusinessAppointment businessAppointment;
    
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeAll
    static void initJunit() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }
    
    @AfterAll
    static void closeJunit() {
        mockedSecurityUtil.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserId).thenReturn("user1");
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserRole).thenReturn("ADMINISTRADOR");
    }

    @Test
    void testGetAll_Admin() {
        List<EntityAppointment> mockList = new ArrayList<>();
        EntityAppointment appointment = new EntityAppointment();
        appointment.setIdAppointment("1");
        appointment.setCode("A-001");
        
        EntitySpecialty specialty = new EntitySpecialty();
        specialty.setName("General");
        appointment.setParentSpecialty(specialty);
        
        EntityDoctor doctor = new EntityDoctor();
        doctor.setFirstName("Luis");
        doctor.setSurName("Rios");
        appointment.setParentDoctor(doctor);
        
        List<EntityAppointmentFile> files = new ArrayList<>();
        EntityAppointmentFile file = new EntityAppointmentFile();
        file.setIdAppointmentfile("file1");
        file.setName("test.pdf");
        file.setExtension("pdf");
        files.add(file);
        appointment.setChildAppointmentFile(files);
        
        List<EntityAppointmentComment> comments = new ArrayList<>();
        EntityAppointmentComment comment = new EntityAppointmentComment();
        comment.setIdAppointmentcomment("comment1");
        comment.setDescription("test comment");
        comment.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
        comments.add(comment);
        appointment.setChildAppointmentComment(comments);
        
        mockList.add(appointment);

        when(repositoryAppointment.findAll()).thenReturn(mockList);

        ResponseAppointmentGetAll response = businessAppointment.getAll();

        assertEquals("success", response.getType());
        assertEquals(1, response.getListAppointment().size());
        assertEquals("A-001", response.getListAppointment().get(0).get("code"));
    }
    
    @Test
    void testGetAll_User() {
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserRole).thenReturn("PACIENTE");
        List<EntityAppointment> mockList = new ArrayList<>();
        EntityAppointment appointment = new EntityAppointment();
        appointment.setIdAppointment("1");
        appointment.setCode("A-002");
        mockList.add(appointment);

        when(repositoryAppointment.findByIdUser("user1")).thenReturn(mockList);

        ResponseAppointmentGetAll response = businessAppointment.getAll();

        assertEquals("success", response.getType());
        assertEquals(1, response.getListAppointment().size());
        assertEquals("A-002", response.getListAppointment().get(0).get("code"));
    }

    @Test
    void testInsert_PastDate() throws IOException {
        RequestAppointmentInsert request = new RequestAppointmentInsert();
        // date from yesterday
        request.setPreferredDate(new Date(System.currentTimeMillis() - 86400000));
        
        ResponseAppointmentInsert response = businessAppointment.insert(request);

        assertEquals("error", response.getType());
        assertEquals("No puede solicitar una cita en una fecha u hora pasada.", response.listMessage.get(0));
    }

    @Test
    void testInsert_Conflict() throws IOException {
        RequestAppointmentInsert request = new RequestAppointmentInsert();
        // date tomorrow
        request.setPreferredDate(new Date(System.currentTimeMillis() + 86400000));
        request.setIdDoctor("doc1");

        when(repositoryAppointment.countConflictingAppointments(eq("doc1"), any(Date.class), any(Date.class), anyString(), anyString())).thenReturn(1);
        
        ResponseAppointmentInsert response = businessAppointment.insert(request);

        assertEquals("error", response.getType());
        assertTrue(response.listMessage.get(0).contains("El doctor ya tiene una cita programada"));
    }

    @Test
    void testInsert_Success() throws IOException {
        RequestAppointmentInsert request = new RequestAppointmentInsert();
        // date tomorrow
        request.setPreferredDate(new Date(System.currentTimeMillis() + 86400000));
        request.setIdDoctor("doc1");

        when(repositoryAppointment.countConflictingAppointments(eq("doc1"), any(Date.class), any(Date.class), anyString(), anyString())).thenReturn(0);
        when(repositoryAppointment.save(any(EntityAppointment.class))).thenReturn(new EntityAppointment());
        
        ResponseAppointmentInsert response = businessAppointment.insert(request);

        assertEquals("success", response.getType());
        assertEquals("Solicitud de cita registrada correctamente.", response.listMessage.get(0));
    }

    @Test
    void testInsert_WithFiles() throws IOException {
        RequestAppointmentInsert request = new RequestAppointmentInsert();
        request.setPreferredDate(new Date(System.currentTimeMillis() + 86400000));
        request.setIdDoctor("doc1");
        
        org.springframework.web.multipart.MultipartFile mockFile = mock(org.springframework.web.multipart.MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.pdf");
        when(mockFile.getInputStream()).thenReturn(new java.io.ByteArrayInputStream("test content".getBytes()));
        
        List<org.springframework.web.multipart.MultipartFile> files = new ArrayList<>();
        files.add(mockFile);
        request.setFiles(files);

        when(repositoryAppointment.countConflictingAppointments(eq("doc1"), any(Date.class), any(Date.class), anyString(), anyString())).thenReturn(0);
        when(repositoryAppointment.save(any(EntityAppointment.class))).thenReturn(new EntityAppointment());
        
        ResponseAppointmentInsert response = businessAppointment.insert(request);

        assertEquals("success", response.getType());
    }

    @Test
    void testSeen() {
        EntityAppointment appointment = new EntityAppointment();
        when(repositoryAppointment.findById("1")).thenReturn(Optional.of(appointment));

        ResponseAppointmentSeen response = businessAppointment.seen("1");

        assertEquals("success", response.getType());
        assertEquals(com.epiis.apicitasmedicas.staticdata.EnumProcess.SEEN.toString(), appointment.getStatus());
    }

    @Test
    void testCoordination() {
        EntityAppointment appointment = new EntityAppointment();
        when(repositoryAppointment.findById("1")).thenReturn(Optional.of(appointment));

        ResponseAppointmentCoordination response = businessAppointment.coordination("1");

        assertEquals("success", response.getType());
        assertEquals(com.epiis.apicitasmedicas.staticdata.EnumProcess.COORDINATION.toString(), appointment.getStatus());
    }

    @Test
    void testReject() {
        EntityAppointment appointment = new EntityAppointment();
        when(repositoryAppointment.findById("1")).thenReturn(Optional.of(appointment));

        ResponseAppointmentReject response = businessAppointment.reject("1");

        assertEquals("success", response.getType());
        assertEquals(com.epiis.apicitasmedicas.staticdata.EnumProcess.REFUSED.toString(), appointment.getStatus());
    }

    @Test
    void testClose() {
        EntityAppointment appointment = new EntityAppointment();
        when(repositoryAppointment.findById("1")).thenReturn(Optional.of(appointment));

        ResponseAppointmentClose response = businessAppointment.close("1");

        assertEquals("success", response.getType());
        assertEquals(com.epiis.apicitasmedicas.staticdata.EnumProcess.CLOSE.toString(), appointment.getStatus());
    }

    @Test
    void testComment() {
        RequestAppointmentComment request = new RequestAppointmentComment();
        request.setIdAppointment("1");
        request.setDescription("Test comment");

        when(repositoryAppointmentComment.save(any(EntityAppointmentComment.class))).thenReturn(new EntityAppointmentComment());

        ResponseAppointmentComment response = businessAppointment.comment(request);

        assertEquals("success", response.getType());
        assertEquals("Comentario registrado correctamente.", response.listMessage.get(0));
    }
}
