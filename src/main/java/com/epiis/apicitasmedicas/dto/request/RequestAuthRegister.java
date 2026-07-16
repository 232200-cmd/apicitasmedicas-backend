package com.epiis.apicitasmedicas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAuthRegister {
    @NotBlank(message = "El campo \"firstName\" es requerido.")
    private String firstName;

    @NotBlank(message = "El campo \"surName\" es requerido.")
    private String surName;

    @NotBlank(message = "El campo \"email\" es requerido.")
    private String email;

    @NotBlank(message = "El campo \"password\" es requerido.")
    private String password;
}
