package com.epiis.apicitasmedicas.dto.request;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAppointmentInsert {
    @NotBlank(message = "El campo \"idSpecialty\" es requerido.")
    private String idSpecialty;

    @NotBlank(message = "El campo \"idDoctor\" es requerido.")
    private String idDoctor;

    @NotBlank(message = "El campo \"personFullName\" es requerido.")
    private String personFullName;

    @NotBlank(message = "El campo \"description\" es requerido.")
    private String description;

    @NotNull(message = "El campo \"preferredDate\" es requerido.")
    private Date preferredDate;

    private List<MultipartFile> files;
}
