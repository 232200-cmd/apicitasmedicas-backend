package com.epiis.apicitasmedicas.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
import com.epiis.apicitasmedicas.helper.GenericHelper;
import com.epiis.apicitasmedicas.repository.RepositoryAppointment;
import com.epiis.apicitasmedicas.repository.RepositoryAppointmentComment;
import com.epiis.apicitasmedicas.repository.RepositoryAppointmentFile;
import com.epiis.apicitasmedicas.security.SecurityUtil;
import com.epiis.apicitasmedicas.staticdata.EnumProcess;

@Service
public class BusinessAppointment {
    private final RepositoryAppointment repositoryAppointment;
    private final RepositoryAppointmentFile repositoryAppointmentFile;
    private final RepositoryAppointmentComment repositoryAppointmentComment;

    public BusinessAppointment(
        RepositoryAppointment repositoryAppointment,
        RepositoryAppointmentFile repositoryAppointmentFile,
        RepositoryAppointmentComment repositoryAppointmentComment
    ) {
        this.repositoryAppointment = repositoryAppointment;
        this.repositoryAppointmentFile = repositoryAppointmentFile;
        this.repositoryAppointmentComment = repositoryAppointmentComment;
    }

    public ResponseAppointmentInsert insert(RequestAppointmentInsert request) throws IOException {
        ResponseAppointmentInsert response = new ResponseAppointmentInsert();

        EntityAppointment entity = new EntityAppointment();
        entity.setIdAppointment(UUID.randomUUID().toString());
        entity.setIdSpecialty(request.getIdSpecialty());
        entity.setIdDoctor(request.getIdDoctor());
        entity.setIdUser(SecurityUtil.getCurrentUserId());
        entity.setCode(GenericHelper.followCodeGeneration());
        entity.setPersonFullName(request.getPersonFullName());
        entity.setDescription(request.getDescription());
        entity.setPreferredDate(request.getPreferredDate());
        entity.setStatus(EnumProcess.PENDING.toString());
        entity.setCreatedAt(new java.sql.Date(new Date().getTime()));
        entity.setUpdatedAt(entity.getCreatedAt());

        repositoryAppointment.save(entity);

        if (request.getFiles() != null) {
            Path basePath = Paths.get("storage/appointmentfile");
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }

            List<EntityAppointmentFile> listFiles = new ArrayList<>();

            for (var file : request.getFiles()) {
                EntityAppointmentFile entityFile = new EntityAppointmentFile();
                entityFile.setIdAppointmentfile(UUID.randomUUID().toString());
                entityFile.setIdAppointment(entity.getIdAppointment());
                entityFile.setName(file.getOriginalFilename());
                entityFile.setExtension(entityFile.getName().substring(entityFile.getName().lastIndexOf(".") + 1).toLowerCase());
                entityFile.setCreatedAt(entity.getCreatedAt());
                entityFile.setUpdatedAt(entity.getCreatedAt());
                listFiles.add(entityFile);
                Files.copy(file.getInputStream(), basePath.resolve(entityFile.getIdAppointmentfile() + "." + entityFile.getExtension()));
            }

            repositoryAppointmentFile.saveAll(listFiles);
        }

        response.success();
        response.listMessage.add("Solicitud de cita registrada correctamente.");
        return response;
    }

    public ResponseAppointmentGetAll getAll() {
        ResponseAppointmentGetAll response = new ResponseAppointmentGetAll();

        String role = SecurityUtil.getCurrentUserRole();
        List<EntityAppointment> list = "ADMINISTRADOR".equalsIgnoreCase(role)
            ? repositoryAppointment.findAll()
            : repositoryAppointment.findByIdUser(SecurityUtil.getCurrentUserId());

        for (EntityAppointment item : list) {
            Map<String, Object> data = new HashMap<>();
            data.put("idAppointment", item.getIdAppointment());
            data.put("code", item.getCode());
            data.put("personFullName", item.getPersonFullName());
            data.put("description", item.getDescription());
            data.put("preferredDate", item.getPreferredDate());
            data.put("status", item.getStatus());
            data.put("specialtyName", item.getParentSpecialty() != null ? item.getParentSpecialty().getName() : "");
            data.put("doctorFullName", item.getParentDoctor() != null ? item.getParentDoctor().getFirstName() + " " + item.getParentDoctor().getSurName() : "");

            List<Map<String, String>> listFiles = new ArrayList<>();
            if (item.getChildAppointmentFile() != null) {
                for (EntityAppointmentFile f : item.getChildAppointmentFile()) {
                    Map<String, String> fileData = new HashMap<>();
                    fileData.put("idAppointmentfile", f.getIdAppointmentfile());
                    fileData.put("name", f.getName());
                    fileData.put("extension", f.getExtension());
                    listFiles.add(fileData);
                }
            }
            data.put("files", listFiles);

            List<Map<String, Object>> listComments = new ArrayList<>();
            if (item.getChildAppointmentComment() != null) {
                for (EntityAppointmentComment c : item.getChildAppointmentComment()) {
                    Map<String, Object> commentData = new HashMap<>();
                    commentData.put("idAppointmentcomment", c.getIdAppointmentcomment());
                    commentData.put("description", c.getDescription());
                    commentData.put("createdAt", c.getCreatedAt());
                    listComments.add(commentData);
                }
            }
            data.put("comments", listComments);

            response.getListAppointment().add(data);
        }

        response.success();
        return response;
    }

    public ResponseAppointmentSeen seen(String idAppointment) {
        ResponseAppointmentSeen response = new ResponseAppointmentSeen();
        repositoryAppointment.findById(idAppointment).ifPresent(entity -> {
            entity.setStatus(EnumProcess.SEEN.toString());
            entity.setUpdatedAt(new java.sql.Date(new Date().getTime()));
            repositoryAppointment.save(entity);
        });
        response.success();
        response.listMessage.add("Cita marcada como vista.");
        return response;
    }

    public ResponseAppointmentCoordination coordination(String idAppointment) {
        ResponseAppointmentCoordination response = new ResponseAppointmentCoordination();
        repositoryAppointment.findById(idAppointment).ifPresent(entity -> {
            entity.setStatus(EnumProcess.COORDINATION.toString());
            entity.setUpdatedAt(new java.sql.Date(new Date().getTime()));
            repositoryAppointment.save(entity);
        });
        response.success();
        response.listMessage.add("Cita en coordinación.");
        return response;
    }

    public ResponseAppointmentReject reject(String idAppointment) {
        ResponseAppointmentReject response = new ResponseAppointmentReject();
        repositoryAppointment.findById(idAppointment).ifPresent(entity -> {
            entity.setStatus(EnumProcess.REFUSED.toString());
            entity.setUpdatedAt(new java.sql.Date(new Date().getTime()));
            repositoryAppointment.save(entity);
        });
        response.success();
        response.listMessage.add("Cita rechazada.");
        return response;
    }

    public ResponseAppointmentClose close(String idAppointment) {
        ResponseAppointmentClose response = new ResponseAppointmentClose();
        repositoryAppointment.findById(idAppointment).ifPresent(entity -> {
            entity.setStatus(EnumProcess.CLOSE.toString());
            entity.setUpdatedAt(new java.sql.Date(new Date().getTime()));
            repositoryAppointment.save(entity);
        });
        response.success();
        response.listMessage.add("Cita cerrada correctamente.");
        return response;
    }

    public ResponseAppointmentComment comment(RequestAppointmentComment request) {
        ResponseAppointmentComment response = new ResponseAppointmentComment();
        EntityAppointmentComment entity = new EntityAppointmentComment();
        entity.setIdAppointmentcomment(UUID.randomUUID().toString());
        entity.setIdAppointment(request.getIdAppointment());
        entity.setIdUser(SecurityUtil.getCurrentUserId());
        entity.setDescription(request.getDescription());
        entity.setCreatedAt(new java.sql.Date(new Date().getTime()));
        entity.setUpdatedAt(entity.getCreatedAt());
        repositoryAppointmentComment.save(entity);
        response.success();
        response.listMessage.add("Comentario registrado correctamente.");
        return response;
    }
}