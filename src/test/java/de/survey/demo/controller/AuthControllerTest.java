package de.survey.demo.controller;

import de.survey.demo.AuthController;
import de.survey.demo.exceptions.handler.ApiExceptionHandler;
import de.survey.demo.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers = AuthController.class)
@Import(ApiExceptionHandler.class) // dein @RestControllerAdvice, falls genutzt
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mvc;
    @MockitoBean
    RegistrationService registrationService;

    @Test
    void returns201OnSuccess() throws Exception {
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"a@b.c\",\"password\":\"Passw0rd!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.next").value("/login"));
    }
}