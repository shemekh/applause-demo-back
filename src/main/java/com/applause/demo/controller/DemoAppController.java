package com.applause.demo.controller;

import com.applause.demo.dto.TesterExpDTO;
import com.applause.demo.entity.Device;
import com.applause.demo.service.DemoService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class DemoAppController {

    private final DemoService demoService;
    public DemoAppController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping(value = "/countries")
    public List<String> getAllCountries () {
        return demoService.getAllCountries();
    }

    @GetMapping(value = "/devices")
    public List<Device> getAllDevices() {
        return demoService.getAllDevices();
    }

    @GetMapping(value = "/testerexp")
    public List<TesterExpDTO> getFilteredTesters(
            @RequestParam(required = false, defaultValue = "") List<String> countries,
            @RequestParam(required = false, defaultValue = "") List<Long> deviceIds
    ) {
        return demoService.getFilteredTesters(countries, deviceIds);
    }
}
