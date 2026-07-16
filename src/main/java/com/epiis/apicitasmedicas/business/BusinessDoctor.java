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
}