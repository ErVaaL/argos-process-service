package com.erval.argos.process.application.port.out;

public interface ResourceQueryPort {
    DeviceInfo getDevice(String deviceId);

    record DeviceInfo(String id, boolean active, boolean found) {
    }
}
