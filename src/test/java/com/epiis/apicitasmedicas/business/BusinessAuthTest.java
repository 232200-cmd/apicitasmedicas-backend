package com.epiis.apicitasmedicas.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epiis.apicitasmedicas.dto.request.RequestAuthLogin;
import com.epiis.apicitasmedicas.dto.request.RequestAuthRegister;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthLogin;
import com.epiis.apicitasmedicas.dto.response.ResponseAuthRegister;
import com.epiis.apicitasmedicas.entity.EntityUser;
import com.epiis.apicitasmedicas.repository.RepositoryUser;
import com.epiis.apicitasmedicas.security.JwtUtil;

class BusinessAuthTest {

    @Mock
    private RepositoryUser repositoryUser;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private BusinessAuth businessAuth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        RequestAuthLogin request = new RequestAuthLogin();
        request.setEmail("test@test.com");
        request.setPassword("password");

        EntityUser user = new EntityUser();
        user.setIdUser("1");
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setRole("Paciente");
        user.setFirstName("Juan");
        user.setSurName("Perez");

        when(repositoryUser.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("1", "test@test.com", "Paciente")).thenReturn("mockedToken");

        ResponseAuthLogin response = businessAuth.login(request);

        assertEquals("success", response.getType());
        assertEquals("mockedToken", response.getToken());
        assertEquals("1", response.getIdUser());
        assertEquals("Inicio de sesión exitoso.", response.listMessage.get(0));
    }

    @Test
    void testLogin_UserNotFound() {
        RequestAuthLogin request = new RequestAuthLogin();
        request.setEmail("notfound@test.com");
        request.setPassword("password");

        when(repositoryUser.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        ResponseAuthLogin response = businessAuth.login(request);

        assertEquals("error", response.getType());
        assertEquals("Correo o contraseña incorrectos.", response.listMessage.get(0));
    }

    @Test
    void testLogin_WrongPassword() {
        RequestAuthLogin request = new RequestAuthLogin();
        request.setEmail("test@test.com");
        request.setPassword("wrongpassword");

        EntityUser user = new EntityUser();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");

        when(repositoryUser.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        ResponseAuthLogin response = businessAuth.login(request);

        assertEquals("error", response.getType());
        assertEquals("Correo o contraseña incorrectos.", response.listMessage.get(0));
    }

    @Test
    void testRegister_Success() {
        RequestAuthRegister request = new RequestAuthRegister();
        request.setEmail("new@test.com");
        request.setPassword("password");
        request.setFirstName("Ana");
        request.setSurName("Gomez");

        when(repositoryUser.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(repositoryUser.save(any(EntityUser.class))).thenReturn(new EntityUser());

        ResponseAuthRegister response = businessAuth.register(request);

        assertEquals("success", response.getType());
        assertEquals("Cuenta registrada correctamente.", response.listMessage.get(0));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        RequestAuthRegister request = new RequestAuthRegister();
        request.setEmail("existing@test.com");

        when(repositoryUser.existsByEmail("existing@test.com")).thenReturn(true);

        ResponseAuthRegister response = businessAuth.register(request);

        assertEquals("error", response.getType());
        assertEquals("Ya existe una cuenta registrada con ese correo.", response.listMessage.get(0));
    }
}
