package com.epiis.apicitasmedicas.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tspecialty")
@Setter
@Getter
public class EntitySpecialty {
    @Id
    @Column(name = "idSpecialty")
    private String idSpecialty;

    @Column(name = "name")
    private String name;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @OneToMany(mappedBy = "parentSpecialty", cascade = CascadeType.ALL)
    private List<EntityAppointment> childAppointment;
}
