package com.applause.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class DemoAppControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_getAllCountries() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc.perform(get("/countries"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then
        List<String> countries = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), String[].class));
        assertThat(countries.size()).isEqualTo(3);
    }
    @Test
    void test_getAllDevices() {
    }

    @Test
    void getFilteredTesters() {
    }
}