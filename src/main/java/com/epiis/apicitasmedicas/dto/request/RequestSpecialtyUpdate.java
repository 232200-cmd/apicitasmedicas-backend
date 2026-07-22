package com.epiis.apicitasmedicas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSpecialtyUpdate {
    @NotBlank(message = "El id de la especialidad es obligatorio.")
    private String idSpecialty;

    @NotBlank(message = "El nombre de la especialidad es obligatorio.")
    private String name;
}
