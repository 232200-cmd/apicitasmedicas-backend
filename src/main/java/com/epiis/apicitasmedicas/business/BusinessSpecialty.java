package com.epiis.apicitasmedicas.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyGetAll;
import com.epiis.apicitasmedicas.entity.EntitySpecialty;
import com.epiis.apicitasmedicas.repository.RepositorySpecialty;

@Service
public class BusinessSpecialty {
    private final RepositorySpecialty repositorySpecialty;

    public BusinessSpecialty(RepositorySpecialty repositorySpecialty) {
        this.repositorySpecialty = repositorySpecialty;
    }

    public ResponseSpecialtyGetAll getAll() {
        ResponseSpecialtyGetAll response = new ResponseSpecialtyGetAll();
        List<EntitySpecialty> list = repositorySpecialty.findAll();
        for (EntitySpecialty item : list) {
            Map<String, Object> data = new HashMap<>();
            data.put("idSpecialty", item.getIdSpecialty());
            data.put("name", item.getName());
            response.getListSpecialty().add(data);
        }
        response.success();
        return response;
    }

    public com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyInsert insert(com.epiis.apicitasmedicas.dto.request.RequestSpecialtyInsert request) {
        com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyInsert response = new com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyInsert();
        
        EntitySpecialty specialty = new EntitySpecialty();
        specialty.setIdSpecialty(java.util.UUID.randomUUID().toString());
        specialty.setName(request.getName());
        specialty.setCreatedAt(new java.util.Date());
        specialty.setUpdatedAt(new java.util.Date());
        
        repositorySpecialty.save(specialty);
        
        response.listMessage.add("Especialidad insertada correctamente.");
        response.success();
        return response;
    }

    public com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyUpdate update(com.epiis.apicitasmedicas.dto.request.RequestSpecialtyUpdate request) {
        com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyUpdate response = new com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyUpdate();
        
        java.util.Optional<EntitySpecialty> optional = repositorySpecialty.findById(request.getIdSpecialty());
        if (optional.isEmpty()) {
            response.listMessage.add("La especialidad no existe.");
            return response;
        }

        EntitySpecialty specialty = optional.get();
        specialty.setName(request.getName());
        specialty.setUpdatedAt(new java.util.Date());
        
        repositorySpecialty.save(specialty);
        
        response.listMessage.add("Especialidad actualizada correctamente.");
        response.success();
        return response;
    }

    public com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyDelete delete(String idSpecialty) {
        com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyDelete response = new com.epiis.apicitasmedicas.dto.response.ResponseSpecialtyDelete();
        
        if (!repositorySpecialty.existsById(idSpecialty)) {
            response.listMessage.add("La especialidad no existe.");
            return response;
        }

        try {
            repositorySpecialty.deleteById(idSpecialty);
            response.listMessage.add("Especialidad eliminada correctamente.");
            response.success();
        } catch (Exception e) {
            response.listMessage.add("No se puede eliminar la especialidad porque está siendo utilizada.");
        }
        
        return response;
    }
}
