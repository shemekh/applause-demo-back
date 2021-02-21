package com.applause.demo.service;

import com.applause.demo.dto.TesterExpDTO;
import com.applause.demo.entity.Device;
import com.applause.demo.entity.Tester;
import com.applause.demo.enums.TesterFilterCriteria;
import com.applause.demo.repository.BugRepository;
import com.applause.demo.repository.DeviceRepository;
import com.applause.demo.repository.TesterRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DemoService {
    private final TesterRepository testerRepository;
    private final DeviceRepository deviceRepository;

    public DemoService(TesterRepository testerRepository, DeviceRepository deviceRepository) {
        this.testerRepository = testerRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<String> getAllCountries() {
        return testerRepository.findAllCountries();
    }

    public List<Device> getAllDevices() {
        return (List<Device>) deviceRepository.findAll();
    }

    public List<TesterExpDTO> getFilteredTesters(List<String> countries, List<Long> deviceIds) {
        List<Tester> testers;
        switch (TesterFilterCriteria.valueOf(!countries.isEmpty(), !deviceIds.isEmpty())) {
            case BOTH:
                testers = testerRepository.findDistinctByCountryInAndDevices_IdIn(countries, deviceIds);
                break;
            case COUNTRY:
                testers = testerRepository.findDistinctByCountryIn(countries);
                break;
            case DEVICE:
                testers = testerRepository.findDistinctByDevices_IdIn(deviceIds);
                break;
            case NONE:
            default:
                testers = (List<Tester>) testerRepository.findAll();
                break;
        }
        return createTesterExpDTOs(testers, deviceIds);
    }

    private List<TesterExpDTO> createTesterExpDTOs(List<Tester> testers, List<Long> deviceIds) {
        return testers.stream()
                .map(tester -> new TesterExpDTO(tester.getFirstName(), tester.getLastName(), getTesterExp(tester, deviceIds)))
                .sorted(Comparator.comparingInt(TesterExpDTO::getExp).reversed())
                .collect(Collectors.toList());
    }

    private int getTesterExp(Tester tester, List<Long> deviceIds) {
        if (deviceIds.isEmpty()) {
            return tester.getBugs().size();
        }
        return (int) tester.getBugs().stream()
                .filter(bug -> deviceIds.contains(bug.getDevice().getId()))
                .count();
    }
}
