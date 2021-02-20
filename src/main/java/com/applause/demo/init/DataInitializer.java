package com.applause.demo.init;

import com.applause.demo.entity.Bug;
import com.applause.demo.entity.Device;
import com.applause.demo.entity.Tester;
import com.applause.demo.properties.CsvProperties;
import com.applause.demo.repository.BugRepository;
import com.applause.demo.repository.DeviceRepository;
import com.applause.demo.repository.TesterRepository;
import com.applause.demo.util.CSVReader;
import com.applause.demo.util.csvmodel.BugCSV;
import com.applause.demo.util.csvmodel.TesterDeviceCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final CsvProperties csvProperties;
    private final TesterRepository testerRepository;
    private final DeviceRepository deviceRepository;
    private final BugRepository bugRepository;

    public DataInitializer(CsvProperties csvProperties, TesterRepository testerRepository,
                           DeviceRepository deviceRepository, BugRepository bugRepository) {
        this.csvProperties = csvProperties;
        this.testerRepository = testerRepository;
        this.deviceRepository = deviceRepository;
        this.bugRepository = bugRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        processCSVFiles();
    }

    private void processCSVFiles() {
        List<Device> devices = CSVReader.readList(Device.class, csvProperties.getDevicesPath());
        List<TesterDeviceCSV> testerDeviceRelations = CSVReader.readList(TesterDeviceCSV.class, csvProperties.getTesterDevicePath());
        List<Tester> testers = CSVReader.readList(Tester.class, csvProperties.getTestersPath());
        List<BugCSV> csvBugs = CSVReader.readList(BugCSV.class, csvProperties.getBugsPath());

        createDevices(devices);
        createTesters(testers, devices, testerDeviceRelations);
        createBugs(csvBugs, testers, devices);
    }

    private void createDevices(List<Device> devices) {
        deviceRepository.saveAll(devices);
    }

    private void createTesters(List<Tester> testers, List<Device> devices, List<TesterDeviceCSV> testerDeviceRelations) {
        for (Tester tester : testers) {

            Set<Long> testerDeviceIds = testerDeviceRelations.stream()
                    .filter(relation -> tester.getId() == relation.getTesterId())
                    .map(relation -> relation.getDeviceId())
                    .collect(Collectors.toSet());

            Set<Device> testerDevices = devices.stream()
                    .filter(device -> testerDeviceIds.contains(device.getId()))
                    .collect(Collectors.toSet());

            tester.setDevices(testerDevices);
        }
        testerRepository.saveAll(testers);
    }

    private void createBugs(List<BugCSV> csvBugs, List<Tester> testers, List<Device> devices) {
        Set<Bug> bugs = csvBugs.stream().map(csvBug -> {
            Optional<Device> optDevice = devices.stream().filter(device -> csvBug.getDeviceId().equals(device.getId())).findAny();
            Optional<Tester> optTester = testers.stream().filter(tester -> csvBug.getTesterId().equals(tester.getId())).findAny();
            Bug bug = new Bug();
            bug.setId(csvBug.getBugId());
            bug.setDevice(optDevice.isPresent() ? optDevice.get() : null);
            bug.setTester(optTester.isPresent() ? optTester.get() : null);

            return bug;
        }).collect(Collectors.toSet());

        bugRepository.saveAll(bugs);
    }
}
