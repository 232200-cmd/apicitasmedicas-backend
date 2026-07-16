package com.epiis.apicitasmedicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epiis.apicitasmedicas.entity.EntityAppointment;

@Repository
public interface RepositoryAppointment extends JpaRepository<EntityAppointment, String> {
    List<EntityAppointment> findByIdUser(String idUser);
}