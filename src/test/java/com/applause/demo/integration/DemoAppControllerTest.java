package com.applause.demo.integration;

import com.applause.demo.dto.TesterExpDTO;
import com.applause.demo.entity.Device;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;


// I'm making general assumption that data from CSV files is test data.
// Thanks to that, I don't need to arrange test data, because
// on every test run application will load same data from csvs.
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
    void test_getAllDevices() throws Exception{
        // given
        // when
        MvcResult mvcResult = mockMvc.perform(get("/devices"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then
        List<Device> devices = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Device[].class));
        assertThat(devices.size()).isEqualTo(10);
    }

    @Test
    void test_getFilteredTestersNoParams() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc.perform(get("/testerexp"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then
        List<TesterExpDTO> testers = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TesterExpDTO[].class));
        assertThat(testers.size()).isEqualTo(9);
        helper_checkTestersOrder(testers);
    }

    @Test
    void test_getFilteredTestersCountriesParam() throws Exception {
        // given
        String[] countries = new String[] {"US", "GB", "JP"};
        // when
        // then
        for (String country : countries) {
            helper_checkTestersWithCountryParam(country, 3);
        }
        helper_checkTestersWithCountryParam(String.join(","), 9);
    }

    @Test
    void test_getFilteredTestersDeviceParam() throws Exception {
        helper_checkTestersWithDeviceIdsParam("1", 4);
        helper_checkTestersWithDeviceIdsParam("2", 2);
        helper_checkTestersWithDeviceIdsParam("7", 3);
        helper_checkTestersWithDeviceIdsParam("1,2,7", 6);
    }

    @Test
    void test_getFilteredTestersBothParams() throws Exception {
        helper_checkTestersWithBothParams("JP",  "1,2", 2);
        helper_checkTestersWithBothParams("JP,GB",  "1,2,4,5", 5);
        helper_checkTestersWithBothParams("GB,US,JP",  "10,9,8,7,4", 7);
        helper_checkTestersWithBothParams("GB,US,JP",  "1,2,3,4,5,6,7,8,9,10", 9);
    }


    // helper methods
    void helper_checkTestersWithBothParams(String countriesParam, String deviceIdsParam, int expectedSize) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/testerexp?deviceIds=" + deviceIdsParam + "&countries=" + countriesParam))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        List<TesterExpDTO> testers = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TesterExpDTO[].class));
        assertThat(testers.size()).isEqualTo(expectedSize);
        helper_checkTestersOrder(testers);
    }

    void helper_checkTestersWithDeviceIdsParam(String deviceIdsParam, int expectedSize) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/testerexp?deviceIds=" + deviceIdsParam))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        List<TesterExpDTO> testers = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TesterExpDTO[].class));
        assertThat(testers.size()).isEqualTo(expectedSize);
        helper_checkTestersOrder(testers);
    }

    void helper_checkTestersWithCountryParam(String countryParam, int expectedSize) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/testerexp?countries=" + countryParam))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        List<TesterExpDTO> testers = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TesterExpDTO[].class));
        assertThat(testers.size()).isEqualTo(expectedSize);
        helper_checkTestersOrder(testers);
    }

    void helper_checkTestersOrder(List<TesterExpDTO> testers) {
        for(int i = 0; i < (testers.size()-1); i++) {
            assertThat(testers.get(i).getExp()).isGreaterThanOrEqualTo(testers.get(i+1).getExp());
        }
    }
}