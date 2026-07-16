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
@Table(name = "tappointmentcomment")
@Setter
@Getter
public class EntityAppointmentComment {
    @Id
    @Column(name = "idAppointmentcomment")
    private String idAppointmentcomment;

    @Column(name = "idAppointment")
    private String idAppointment;

    @Column(name = "idUser")
    private String idUser;

    @Column(name = "description")
    private String description;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAppointment", insertable = false, updatable = false)
    private EntityAppointment parentAppointment;
}
