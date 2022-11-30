package com.backend.dove.e2e;

import com.backend.dove.dto.UserInfoDto;
import com.backend.dove.dto.UserRegisterDto;
import com.backend.dove.entity.User;
import com.backend.dove.repository.UserRepository;
import com.backend.dove.util.WiserAssertions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.subethamail.wiser.Wiser;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
public class AccountTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    Wiser wiser;

    @Before
    public void setUp() throws Exception {
        wiser = new Wiser();
        wiser.setHostname("localhost");
        wiser.setPort(2500);
        wiser.start();
    }
 
    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }
    
    static final UserRegisterDto registerDto = new UserRegisterDto()
            .setEmail("alexOfTheBlock@gmail.com")
            .setUsername("alexOfTheBlock")
            .setPassword("3oarh83oahsdjka5");


    private ResultActions register() throws Exception {
        return mvc.perform(
                post("/api/user/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDto))
                        .with(csrf())
        );
    }

    private ResultActions login() throws Exception {
        return mvc.perform(
                post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto.toLogin()))
                        .with(csrf())
        );
    }

    private ResultActions logout() throws Exception {
        return this.mvc.perform(
                post("/api/user/logout")
                        .with(csrf())
        );
    }

    @Test
    public void testRegistration() throws Exception {
        register().andExpect(
                status().isOk()
        );

        var user = userRepository.getUserByEmail("alexOfTheBlock@gmail.com");
        userRepository.delete(user);

        assert(user.getEmail()
                .equals(registerDto.getEmail()));
        assert(user.getUsername()
                .equals(registerDto.getUsername()));
        assert(passwordEncoder.matches(registerDto.getPassword(), user.getPassword()));
    }

    @Test
    public void testRegistrationEmail() throws Exception {
        register().andExpect(
                status().isOk()
        );

        try {
            WiserAssertions.assertReceivedMessage(wiser)
                    .to(registerDto.getEmail())
                    .withSubject("Finish your registration")
                    .withContent(content -> {
                        try {
                            var dom = Jsoup.parse(content);
                            System.out.println(content);

                            var href = dom.body()
                                    .select("a")
                                    .attr("href");

                            var span = dom.body()
                                    .select("span")
                                    .text();

                            return href.contains("/verify?email=")
                                    && href.contains("&token=")
                                    && span.equals(registerDto.getUsername());
                        } catch (Exception e) {
                            return false;
                        }
                    });
        } finally {
            userRepository.deleteUserByEmail(registerDto.getEmail());
        }
    }

    @Test
    @DirtiesContext
    public void testLogin() throws Exception {
        logout().andExpect(
                status().isForbidden()
        );

        register().andExpect(
                status().isOk()
        );

        try {
            login().andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(registerDto.getEmail()))
                    .andExpect(jsonPath("$.username").value(registerDto.getUsername()));

            logout().andExpect(
                    status().isOk()
            );
        } finally {
            userRepository.deleteUserByEmail(registerDto.getEmail());
        }
    }
}
