package com.epiis.apicitasmedicas.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epiis.apicitasmedicas.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAppointmentGetAll extends ResponseGeneric {
    List<Map<String, Object>> listAppointment = new ArrayList<>();
}
