package com.epiis.apicitasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epiis.apicitasmedicas.entity.EntityAppointmentFile;

@Repository
public interface RepositoryAppointmentFile extends JpaRepository<EntityAppointmentFile, String> {}
