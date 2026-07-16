package com.epiis.apicitasmedicas.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tappointment")
@Setter
@Getter
public class EntityAppointment {
    @Id
    @Column(name = "idAppointment")
    private String idAppointment;

    @Column(name = "idSpecialty")
    private String idSpecialty;

    @Column(name = "idDoctor")
    private String idDoctor;

    @Column(name = "idUser")
    private String idUser;

    @Column(name = "code")
    private String code;

    @Column(name = "personFullName")
    private String personFullName;

    @Column(name = "description")
    private String description;

    @Column(name = "preferredDate")
    private Date preferredDate;

    @Column(name = "status")
    private String status;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSpecialty", insertable = false, updatable = false)
    private EntitySpecialty parentSpecialty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDoctor", insertable = false, updatable = false)
    private EntityDoctor parentDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", insertable = false, updatable = false)
    private EntityUser parentUser;

    @OneToMany(mappedBy = "parentAppointment", cascade = CascadeType.ALL)
    private List<EntityAppointmentFile> childAppointmentFile;

    @OneToMany(mappedBy = "parentAppointment", cascade = CascadeType.ALL)
    private List<EntityAppointmentComment> childAppointmentComment;
}
