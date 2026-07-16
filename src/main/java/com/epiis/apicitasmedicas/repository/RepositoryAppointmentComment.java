package com.epiis.apicitasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epiis.apicitasmedicas.entity.EntityAppointmentComment;

@Repository
public interface RepositoryAppointmentComment extends JpaRepository<EntityAppointmentComment, String> {}
