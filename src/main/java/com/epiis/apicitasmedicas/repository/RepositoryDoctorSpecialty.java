package com.epiis.apicitasmedicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epiis.apicitasmedicas.entity.EntityDoctorSpecialty;

@Repository
public interface RepositoryDoctorSpecialty extends JpaRepository<EntityDoctorSpecialty, String> {
    List<EntityDoctorSpecialty> findByIdSpecialty(String idSpecialty);
    List<EntityDoctorSpecialty> findByIdDoctor(String idDoctor);
}
