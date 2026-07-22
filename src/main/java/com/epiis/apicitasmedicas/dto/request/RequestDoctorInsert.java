package com.epiis.apicitasmedicas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDoctorInsert {
    @NotBlank(message = "El nombre del doctor es obligatorio.")
    private String firstName;

    @NotBlank(message = "El apellido del doctor es obligatorio.")
    private String surName;

    @NotBlank(message = "El email del doctor es obligatorio.")
    @Email(message = "Debe ser un email válido.")
    private String email;

    @NotBlank(message = "El teléfono del doctor es obligatorio.")
    private String phoneNumber;

    @NotBlank(message = "La especialidad es obligatoria.")
    private String idSpecialty;
}
