package com.erval.argos.process.application.port.out;

public interface ResourceQueryPort {
    DeviceInfo getDevice(String deviceId);

    record DeviceInfo(String id,String name, boolean active, boolean found) {
    }
}
