package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.LoginReqDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    private final Role userRole = Role.USER;
    private final LoginReqDto req = new LoginReqDto();
    private final RegisterReqDto reqReg = new RegisterReqDto();


    @BeforeEach
    public void setUp() {
        req.setUsername("testuser");
        req.setPassword("testpassword");

        reqReg.setUsername("testuser");
        reqReg.setPassword("testpassword");
    }

    @Test
    @DisplayName("Проверка успешного входа в учетную запись")
    public void loginSuccessTest() {
        when(authService.login(req.getUsername(), req.getPassword())).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.login(req);

        verify(authService).login(req.getUsername(), req.getPassword());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Проверка невозможности входа в учетную запись")
    void loginFailureTest() {
        when(authService.login(req.getUsername(), req.getPassword())).thenReturn(false);

        ResponseEntity<?> responseEntity = authController.login(req);

        verify(authService).login(req.getUsername(), req.getPassword());
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Проверка успешности регистрации на сайте")
    public void registerSuccessTest() {
        when(authService.register(reqReg, userRole)).thenReturn(true);

        ResponseEntity<?> response = authController.register(reqReg);

        verify(authService).register(reqReg, userRole);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Проверка провальной регистрации на сайте")
    void registerFailureTest() {
        when(authService.register(reqReg, userRole)).thenReturn(false);

        ResponseEntity<?> response = authController.register(reqReg);

        verify(authService).register(reqReg, userRole);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
