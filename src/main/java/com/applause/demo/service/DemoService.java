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
    private final BugRepository bugRepository;

    public DemoService(TesterRepository testerRepository, DeviceRepository deviceRepository,
                       BugRepository bugRepository) {
        this.testerRepository = testerRepository;
        this.deviceRepository = deviceRepository;
        this.bugRepository = bugRepository;
    }

    public List<String> getAllCountries() {
        return testerRepository.findAllCountries();
    }

    public List<Device> getAllDevices() {
        return (List<Device>) deviceRepository.findAll();
    }

    public List<TesterExpDTO> getFilteredTesters(Optional<List<String>> countries, Optional<List<Long>> deviceIds) {
        List<Tester> testers;
        switch (TesterFilterCriteria.valueOf(countries.isPresent(), deviceIds.isPresent())) {
            case BOTH:
                testers = testerRepository.findDistinctByCountryInAndDevices_IdIn(countries.get(), deviceIds.get());
                break;
            case COUNTRY:
                testers = testerRepository.findDistinctByCountryIn(countries.get());
                break;
            case DEVICE:
                testers = testerRepository.findDistinctByDevices_IdIn(deviceIds.get());
                break;
            case NONE:
            default:
                testers = Collections.emptyList();
                break;
        }

        return createTesterExpDTOs(testers, deviceIds.orElse(Collections.emptyList()));
    }

    private List<TesterExpDTO> createTesterExpDTOs(List<Tester> testers, List<Long> deviceIds) {
        return testers.stream()
                .map(tester -> new TesterExpDTO(tester.getFirstName(), tester.getLastName(), getTesterExp(tester, deviceIds)))
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
