package com.epiis.apicitasmedicas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAppointmentComment {
    @NotBlank(message = "El campo \"idAppointment\" es requerido.")
    private String idAppointment;

    @NotBlank(message = "El campo \"description\" es requerido.")
    private String description;
}