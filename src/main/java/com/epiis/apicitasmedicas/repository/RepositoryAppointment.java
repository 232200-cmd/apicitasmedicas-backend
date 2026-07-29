package com.epiis.apicitasmedicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.epiis.apicitasmedicas.entity.EntityAppointment;
import java.util.Date;

@Repository
public interface RepositoryAppointment extends JpaRepository<EntityAppointment, String> {
    List<EntityAppointment> findByIdUser(String idUser);

    @Query("SELECT COUNT(a) FROM EntityAppointment a WHERE a.idDoctor = :idDoctor AND a.preferredDate >= :startTime AND a.preferredDate <= :endTime AND a.status != :statusRefused AND a.status != :statusClose")
    int countConflictingAppointments(@Param("idDoctor") String idDoctor, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("statusRefused") String statusRefused, @Param("statusClose") String statusClose);
}