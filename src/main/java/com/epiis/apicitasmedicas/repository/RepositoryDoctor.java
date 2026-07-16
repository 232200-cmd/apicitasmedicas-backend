package com.epiis.apicitasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epiis.apicitasmedicas.entity.EntityDoctor;

@Repository
public interface RepositoryDoctor extends JpaRepository<EntityDoctor, String> {}
