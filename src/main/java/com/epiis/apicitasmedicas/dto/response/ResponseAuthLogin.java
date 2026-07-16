package com.epiis.apicitasmedicas.dto.response;

import com.epiis.apicitasmedicas.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAuthLogin extends ResponseGeneric {
    private String token;
    private String idUser;
    private String firstName;
    private String surName;
    private String email;
    private String role;
}
