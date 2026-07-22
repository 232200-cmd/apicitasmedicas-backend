package com.epiis.apicitasmedicas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSpecialtyInsert {
    @NotBlank(message = "El nombre de la especialidad es obligatorio.")
    private String name;
}
