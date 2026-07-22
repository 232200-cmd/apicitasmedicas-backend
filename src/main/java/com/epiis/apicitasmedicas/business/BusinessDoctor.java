package com.epiis.apicitasmedicas.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetAll;
import com.epiis.apicitasmedicas.dto.response.ResponseDoctorGetBySpecialty;
import com.epiis.apicitasmedicas.entity.EntityDoctor;
import com.epiis.apicitasmedicas.entity.EntityDoctorSpecialty;
import com.epiis.apicitasmedicas.repository.RepositoryDoctor;
import com.epiis.apicitasmedicas.repository.RepositoryDoctorSpecialty;

@Service
public class BusinessDoctor {
    private final RepositoryDoctor repositoryDoctor;
    private final RepositoryDoctorSpecialty repositoryDoctorSpecialty;

    public BusinessDoctor(RepositoryDoctor repositoryDoctor, RepositoryDoctorSpecialty repositoryDoctorSpecialty) {
        this.repositoryDoctor = repositoryDoctor;
        this.repositoryDoctorSpecialty = repositoryDoctorSpecialty;
    }

    public ResponseDoctorGetAll getAll() {
        ResponseDoctorGetAll response = new ResponseDoctorGetAll();
        List<EntityDoctor> list = repositoryDoctor.findAll();
        for (EntityDoctor item : list) {
            Map<String, Object> data = new HashMap<>();
            data.put("idDoctor", item.getIdDoctor());
            data.put("firstName", item.getFirstName());
            data.put("surName", item.getSurName());
            data.put("email", item.getEmail());
            data.put("fullName", item.getFirstName() + " " + item.getSurName());
            response.getListDoctor().add(data);
        }
        response.success();
        return response;
    }

    public ResponseDoctorGetBySpecialty getBySpecialty(String idSpecialty) {
        ResponseDoctorGetBySpecialty response = new ResponseDoctorGetBySpecialty();

        List<EntityDoctorSpecialty> relations = repositoryDoctorSpecialty.findByIdSpecialty(idSpecialty);

        for (EntityDoctorSpecialty relation : relations) {
            EntityDoctor doctor = relation.getParentDoctor();
            if (doctor != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("idDoctor", doctor.getIdDoctor());
                data.put("firstName", doctor.getFirstName());
                data.put("surName", doctor.getSurName());
                data.put("email", doctor.getEmail());
                data.put("fullName", doctor.getFirstName() + " " + doctor.getSurName());
                response.getListDoctor().add(data);
            }
        }

        response.success();
        return response;
    }

    public com.epiis.apicitasmedicas.dto.response.ResponseDoctorInsert insert(com.epiis.apicitasmedicas.dto.request.RequestDoctorInsert request) {
        com.epiis.apicitasmedicas.dto.response.ResponseDoctorInsert response = new com.epiis.apicitasmedicas.dto.response.ResponseDoctorInsert();
        
        EntityDoctor doctor = new EntityDoctor();
        doctor.setIdDoctor(java.util.UUID.randomUUID().toString());
        doctor.setFirstName(request.getFirstName());
        doctor.setSurName(request.getSurName());
        doctor.setEmail(request.getEmail());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setCreatedAt(new java.util.Date());
        doctor.setUpdatedAt(new java.util.Date());
        
        repositoryDoctor.save(doctor);

        EntityDoctorSpecialty doctorSpecialty = new EntityDoctorSpecialty();
        doctorSpecialty.setIdDoctorSpecialty(java.util.UUID.randomUUID().toString());
        doctorSpecialty.setIdDoctor(doctor.getIdDoctor());
        doctorSpecialty.setIdSpecialty(request.getIdSpecialty());
        doctorSpecialty.setCreatedAt(new java.util.Date());
        doctorSpecialty.setUpdatedAt(new java.util.Date());
        
        repositoryDoctorSpecialty.save(doctorSpecialty);
        
        response.listMessage.add("Doctor insertado correctamente.");
        response.success();
        return response;
    }

    public com.epiis.apicitasmedicas.dto.response.ResponseDoctorUpdate update(com.epiis.apicitasmedicas.dto.request.RequestDoctorUpdate request) {
        com.epiis.apicitasmedicas.dto.response.ResponseDoctorUpdate response = new com.epiis.apicitasmedicas.dto.response.ResponseDoctorUpdate();
        
        java.util.Optional<EntityDoctor> optional = repositoryDoctor.findById(request.getIdDoctor());
        if (optional.isEmpty()) {
            response.listMessage.add("El doctor no existe.");
            return response;
        }

        EntityDoctor doctor = optional.get();
        doctor.setFirstName(request.getFirstName());
        doctor.setSurName(request.getSurName());
        doctor.setEmail(request.getEmail());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setUpdatedAt(new java.util.Date());
        
        repositoryDoctor.save(doctor);
        
        // Update specialty
        List<EntityDoctorSpecialty> relations = repositoryDoctorSpecialty.findByIdDoctor(request.getIdDoctor());
        if (!relations.isEmpty()) {
            EntityDoctorSpecialty doctorSpecialty = relations.get(0);
            if (!doctorSpecialty.getIdSpecialty().equals(request.getIdSpecialty())) {
                doctorSpecialty.setIdSpecialty(request.getIdSpecialty());
                doctorSpecialty.setUpdatedAt(new java.util.Date());
                repositoryDoctorSpecialty.save(doctorSpecialty);
            }
        } else {
            EntityDoctorSpecialty doctorSpecialty = new EntityDoctorSpecialty();
            doctorSpecialty.setIdDoctorSpecialty(java.util.UUID.randomUUID().toString());
            doctorSpecialty.setIdDoctor(doctor.getIdDoctor());
            doctorSpecialty.setIdSpecialty(request.getIdSpecialty());
            doctorSpecialty.setCreatedAt(new java.util.Date());
            doctorSpecialty.setUpdatedAt(new java.util.Date());
            repositoryDoctorSpecialty.save(doctorSpecialty);
        }
        
        response.listMessage.add("Doctor actualizado correctamente.");
        response.success();
        return response;
    }

    public com.epiis.apicitasmedicas.dto.response.ResponseDoctorDelete delete(String idDoctor) {
        com.epiis.apicitasmedicas.dto.response.ResponseDoctorDelete response = new com.epiis.apicitasmedicas.dto.response.ResponseDoctorDelete();
        
        if (!repositoryDoctor.existsById(idDoctor)) {
            response.listMessage.add("El doctor no existe.");
            return response;
        }

        try {
            // Eliminar relacion primero
            List<EntityDoctorSpecialty> relations = repositoryDoctorSpecialty.findByIdDoctor(idDoctor);
            repositoryDoctorSpecialty.deleteAll(relations);
            
            repositoryDoctor.deleteById(idDoctor);
            
            response.listMessage.add("Doctor eliminado correctamente.");
            response.success();
        } catch (Exception e) {
            response.listMessage.add("No se puede eliminar el doctor porque está siendo utilizado.");
        }
        
        return response;
    }
}