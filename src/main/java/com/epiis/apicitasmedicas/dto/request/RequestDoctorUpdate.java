package com.epiis.apicitasmedicas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDoctorUpdate extends RequestDoctorBase {
    @NotBlank(message = "El id del doctor es obligatorio.")
    private String idDoctor;
}
