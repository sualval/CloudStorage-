package ru.netology.cloudstorage.controllersTest;

import ru.netology.cloudstorage.controllers.AuthController;
import ru.netology.cloudstorage.dto.request.SignupRequest;
import ru.netology.cloudstorage.entities.Role;
import ru.netology.cloudstorage.entities.User;
import ru.netology.cloudstorage.security.UserDetailsService;
import ru.netology.cloudstorage.security.jwt.JwtAuthenticationEntryPoint;
import ru.netology.cloudstorage.security.jwt.JwtTokenFilter;
import ru.netology.cloudstorage.security.jwt.JwtUtils;
import ru.netology.cloudstorage.service.impl.UserServiceImpl;
import ru.netology.cloudstorage.util.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {
    @InjectMocks
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @MockBean
    private UserServiceImpl userService;

    @MockBean(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserMapper userMapper;


    @Test
    @DisplayName("Test signup endpoint returns HTTP.Status 200")

    public void saveUser() throws Exception {
        User user = new User("name", "1234", Role.ROLE_USER);
        user.setId(1L);
        SignupRequest signupRequest = new SignupRequest(user.getLogin(), user.getPassword());
        when(userMapper.convertToSignupUser(signupRequest)).thenReturn(user);
        doNothing().when(userService).registration(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(signupRequest);
        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("Test login endpoint returns HTTP.Status 200")
    void login() throws Exception {
        when(jwtUtils.generateToken("user")).thenReturn("Bearer jwt");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user\", \"password\": \"1234\"}"))

                .andDo(print()).andExpect(status().isOk());

    }


    @Test
    @WithMockUser
    @DisplayName(" Test logout endpoint returns successful redirection to auth_login")
    void logout() throws Exception {

        mockMvc.perform(post("/logout")
                        .header("auth-token", "Bearer jwt"))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));

    }


}
