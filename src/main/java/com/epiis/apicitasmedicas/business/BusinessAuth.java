package com.epiis.apicitasmedicas.business;

import java.util.Date;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.epiis.apicitasmedicas.dto.request.RequestAuthLogin;
import com.epiis.apicitasmedicas.dto.request.RequestAuthRegister;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthLogin;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthRegister;
import com.epiis.apicitasmedicas.entity.EntityUser;
import com.epiis.apicitasmedicas.repository.RepositoryUser;
import com.epiis.apicitasmedicas.security.JwtUtil;

@Service
public class BusinessAuth {
    private final RepositoryUser repositoryUser;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public BusinessAuth(RepositoryUser repositoryUser, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repositoryUser = repositoryUser;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseAuthLogin login(RequestAuthLogin request) {
        ResponseAuthLogin response = new ResponseAuthLogin();

        var userOptional = repositoryUser.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            response.error();
            response.listMessage.add("Correo o contraseña incorrectos.");
            return response;
        }

        EntityUser user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.error();
            response.listMessage.add("Correo o contraseña incorrectos.");
            return response;
        }

        String token = jwtUtil.generateToken(user.getIdUser(), user.getEmail(), user.getRole());

        response.setToken(token);
        response.setIdUser(user.getIdUser());
        response.setFirstName(user.getFirstName());
        response.setSurName(user.getSurName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        response.success();
        response.listMessage.add("Inicio de sesión exitoso.");
        return response;
    }

    public ResponseAuthRegister register(RequestAuthRegister request) {
        ResponseAuthRegister response = new ResponseAuthRegister();

        if (repositoryUser.existsByEmail(request.getEmail())) {
            response.error();
            response.listMessage.add("Ya existe una cuenta registrada con ese correo.");
            return response;
        }

        EntityUser entity = new EntityUser();
        entity.setIdUser(UUID.randomUUID().toString());
        entity.setFirstName(request.getFirstName());
        entity.setSurName(request.getSurName());
        entity.setEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole("Paciente");
        entity.setCreatedAt(new java.sql.Date(new Date().getTime()));
        entity.setUpdatedAt(entity.getCreatedAt());

        repositoryUser.save(entity);

        response.success();
        response.listMessage.add("Cuenta registrada correctamente.");
        return response;
    }
}
