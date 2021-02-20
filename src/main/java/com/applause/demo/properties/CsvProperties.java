package com.applause.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "csv")
@ConstructorBinding
public class CsvProperties {
    private String testersPath;
    private String devicesPath;
    private String bugsPath;
    private String testerDevicePath;

    public CsvProperties(String testersPath, String devicesPath, String bugsPath, String testerDevicePath) {
        this.testersPath = testersPath;
        this.devicesPath = devicesPath;
        this.bugsPath = bugsPath;
        this.testerDevicePath = testerDevicePath;
    }

    public String getTestersPath() {
        return testersPath;
    }

    public String getDevicesPath() {
        return devicesPath;
    }

    public String getBugsPath() {
        return bugsPath;
    }

    public String getTesterDevicePath() {
        return testerDevicePath;
    }
}
