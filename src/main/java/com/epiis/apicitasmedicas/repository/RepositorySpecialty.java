package com.epiis.apicitasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epiis.apicitasmedicas.entity.EntitySpecialty;

@Repository
public interface RepositorySpecialty extends JpaRepository<EntitySpecialty, String> {}
