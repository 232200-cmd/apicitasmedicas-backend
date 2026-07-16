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
}
