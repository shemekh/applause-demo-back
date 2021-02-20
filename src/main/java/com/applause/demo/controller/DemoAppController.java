package com.applause.demo.controller;

import com.applause.demo.dto.TesterExpDTO;
import com.applause.demo.entity.Device;
import com.applause.demo.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(required = false) Optional<List<String>> countries,
            @RequestParam(required = false) Optional<List<Long>> deviceIds
    ) {
        return demoService.getFilteredTesters(countries, deviceIds);
    }
}
