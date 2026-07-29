package com.epiis.apicitasmedicas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDoctorInsert {
    @NotBlank(message = "El nombre del doctor es obligatorio.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios.")
    private String firstName;

    @NotBlank(message = "El apellido del doctor es obligatorio.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios.")
    private String surName;

    @NotBlank(message = "El email del doctor es obligatorio.")
    @Email(message = "Debe ser un email válido.")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres.")
    private String email;

    @NotBlank(message = "El teléfono del doctor es obligatorio.")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe contener exactamente 9 dígitos.")
    private String phoneNumber;

    @NotBlank(message = "La especialidad es obligatoria.")
    private String idSpecialty;
}
