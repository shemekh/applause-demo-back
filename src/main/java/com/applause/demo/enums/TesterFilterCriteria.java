package com.applause.demo.enums;

import java.util.Arrays;

public enum TesterFilterCriteria {
    BOTH(true, true),
    COUNTRY(true, false),
    DEVICE(false, true),
    NONE(false, false);

    private boolean isCountry;
    private boolean isDevice;

    TesterFilterCriteria(boolean isCountry, boolean isDevice) {
        this.isCountry = isCountry;
        this.isDevice = isDevice;
    }

    public static TesterFilterCriteria valueOf(boolean isCountry, boolean isDevice) {
        return Arrays.stream(TesterFilterCriteria.values())
                .filter(filterCriteria -> filterCriteria.isCountry == isCountry && filterCriteria.isDevice == isDevice)
                .findAny()
                .get();
    }
}
