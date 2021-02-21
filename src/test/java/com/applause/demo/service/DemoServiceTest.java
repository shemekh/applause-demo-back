package com.applause.demo.service;

import com.applause.demo.dto.TesterExpDTO;
import com.applause.demo.entity.Bug;
import com.applause.demo.entity.Device;
import com.applause.demo.entity.Tester;
import com.applause.demo.repository.DeviceRepository;
import com.applause.demo.repository.TesterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class DemoServiceTest {
    @InjectMocks
    DemoService demoService;
    @Mock
    TesterRepository testerRepository;
    @Mock
    DeviceRepository deviceRepository;

    @Test
    void test_getAllCountries() {
        // given
        List<String> mockedCountries = Arrays.asList(new String[] { "US", "JP"});

        when(testerRepository.findAllCountries()).thenReturn(mockedCountries);
        // when
        List<String> countries = demoService.getAllCountries();
        // then
        assertThat(countries.size()).isEqualTo(2);
    }

    @Test
    void test_getAllDevices() {
        // given
        Device device_1 = getDevice(1L, "device_1");
        Device device_2 = getDevice(2L, "device_2");
        Device device_3 = getDevice(3L, "device_3");
        List<Device> mockedDevices = Arrays.asList(new Device[]{device_1, device_2, device_3});

        when(deviceRepository.findAll()).thenReturn(mockedDevices);
        // when
        List<Device> devices = demoService.getAllDevices();
        // then
        assertThat(devices.size()).isEqualTo(3);
    }

    @Test
    void test_getFilteredTesters_WithNoParams() {
        // given
        Tester tester = arrangeTester1();
        Tester tester_2 = arrangeTester2();
        when(testerRepository.findAll()).thenReturn(Arrays.asList(new Tester[] { tester, tester_2 }));
        // when
        List<TesterExpDTO> dtoTestersExp = demoService.getFilteredTesters(Collections.emptyList(), Collections.emptyList());
        // then
        assertThat(dtoTestersExp.size()).isEqualTo(2);
        assertThat(dtoTestersExp.get(0).getExp()).isGreaterThan(dtoTestersExp.get(1).getExp());
        assertThat(dtoTestersExp.get(0).getExp()).isEqualTo(15);
        assertThat(dtoTestersExp.get(1).getExp()).isEqualTo(2);
    }

    @Test
    void test_getFilteredTesters_WithBothParams() {
        // given
        Tester tester = arrangeTester1();

        List<String> countries = Arrays.asList(new String[] { "UK" });

        List<Long> deviceIds = Arrays.asList(new Long[] { 1L, 2L });
        List<Long> deviceIds_2 = Arrays.asList(new Long[] { 1L });

        when(testerRepository.findDistinctByCountryInAndDevices_IdIn(countries, deviceIds))
                .thenReturn(Arrays.asList(new Tester[] { tester }));
        when(testerRepository.findDistinctByCountryInAndDevices_IdIn(countries, deviceIds_2))
                .thenReturn(Arrays.asList(new Tester[] { tester }));
        // when
        List<TesterExpDTO> dtoTestersExp = demoService.getFilteredTesters(countries, deviceIds);
        List<TesterExpDTO> dtoTestersExp_2 = demoService.getFilteredTesters(countries, deviceIds_2);

        // then
        assertThat(dtoTestersExp.size()).isEqualTo(1);
        assertThat(dtoTestersExp.get(0).getExp()).isEqualTo(15);

        assertThat(dtoTestersExp_2.size()).isEqualTo(1);
        assertThat(dtoTestersExp_2.get(0).getExp()).isEqualTo(5);
    }

    @Test
    void test_getFilteredTesters_WithCountryParam() {
        // given
        Tester tester = arrangeTester1();

        List<String> countries = Arrays.asList(new String[] { "UK" });

        when(testerRepository.findDistinctByCountryIn(countries))
                .thenReturn(Arrays.asList(new Tester[] { tester }));
        // when
        List<TesterExpDTO> dtoTestersExp = demoService.getFilteredTesters(countries, Collections.emptyList());

        // then
        assertThat(dtoTestersExp.size()).isEqualTo(1);
        assertThat(dtoTestersExp.get(0).getExp()).isEqualTo(15);
    }

    @Test
    void test_getFilteredTesters_WithDeviceIdsParam() {
        // given
        Tester tester = arrangeTester1();

        List<Long> deviceIds = Arrays.asList(new Long[] { 2L });

        when(testerRepository.findDistinctByDevices_IdIn(deviceIds))
                .thenReturn(Arrays.asList(new Tester[] { tester }));
        // when
        List<TesterExpDTO> dtoTestersExp = demoService.getFilteredTesters(Collections.emptyList(), deviceIds);

        // then
        assertThat(dtoTestersExp.size()).isEqualTo(1);
        assertThat(dtoTestersExp.get(0).getExp()).isEqualTo(10);
    }

    Tester arrangeTester1() {
        // setup tester 1
        Device device_1 = getDevice(1L, "Anything");
        Device device_2 = getDevice(2L, "Anything2");
        Set<Device> devices = new HashSet<>();
        devices.add(device_1);
        devices.add(device_2);
        Set<Bug> bugs_1 = getMockedBugsForDevice(device_1, 5);
        Set<Bug> bugs_2 = getMockedBugsForDevice(device_2, 10);
        Tester tester = getTester(1L, "Alan", "Brick", "UK",
                devices, joinSets(bugs_1, bugs_2));
        return tester;
        // Alan Brick
        // 5 bugs for device 1L
        // 10 bugs for device 2L
    }

    Tester arrangeTester2() {
        //setup tester 2
        Device device_3 = getDevice(3L, "Anything3");
        Set<Device> devices_2 = new HashSet<>();
        devices_2.add(device_3);
        Set<Bug> bugs_3 = getMockedBugsForDevice(device_3, 2);

        Tester tester_2 = getTester(2L, "Mark", "Green", "US", devices_2, bugs_3);
        return tester_2;
        // Mark Green
        // 2 bugs for device 3L
    }

    // helper methods
    Device getDevice(Long id, String desc) {
        Device device = new Device();
        device.setId(id);
        device.setDescription(desc);

        return device;
    }

    Tester getTester(Long id, String firstName, String lastName, String country, Set<Device> devices, Set<Bug> bugs) {
        Tester tester = new Tester();
        tester.setId(id);
        tester.setFirstName(firstName);
        tester.setLastName(lastName);
        tester.setCountry(country);
        tester.setDevices(devices);
        tester.setBugs(bugs);

        return tester;
    }

    public static long lastBugId = 0;
    Set<Bug> getMockedBugsForDevice(Device device, int amount) {
        Set<Bug> bugs = new HashSet<>();
        for(int i = 0; i < amount; i++) {
            Bug bug = new Bug();
            bug.setId(++lastBugId);
            bug.setDevice(device);
            bugs.add(bug);
        }

        return bugs;
    }

    public static <T> Set<T> joinSets(Set<T>... sets) {
        return Arrays.stream(sets).flatMap(Collection::stream).collect(Collectors.toSet());
    }
}