package com.epiis.apicitasmedicas.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tdoctorspecialty")
@Setter
@Getter
public class EntityDoctorSpecialty {
    @Id
    @Column(name = "idDoctorSpecialty")
    private String idDoctorSpecialty;

    @Column(name = "idDoctor")
    private String idDoctor;

    @Column(name = "idSpecialty")
    private String idSpecialty;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDoctor", insertable = false, updatable = false)
    private EntityDoctor parentDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSpecialty", insertable = false, updatable = false)
    private EntitySpecialty parentSpecialty;
}