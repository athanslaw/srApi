package com.edunge.srtool;

import com.edunge.srtool.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes=SrToolApplication.class)
@ActiveProfiles("local")
public class UserDetailsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsTest.class);

    private static final UserDto userDto = new UserDto();
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeClass
    public static void deleteTestFiles(){
        userDto.setPhone("08067413041");
        userDto.setFirstName("Adewale");
        userDto.setLastName("Adeleye");
        userDto.setPassword("testPassword");
        userDto.setEmail("admin_test@gmail.com");
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void registerNewUser() throws IOException {
        String requestContent = MAPPER.writeValueAsString(userDto);
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/v1/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestContent))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andReturn();
            LOGGER.info(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void userAlreadyExists() throws IOException {
        String requestContent = MAPPER.writeValueAsString(userDto);
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/v1/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestContent))
                    .andExpect(MockMvcResultMatchers.status().is(409))
                    .andReturn();
            LOGGER.info(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
