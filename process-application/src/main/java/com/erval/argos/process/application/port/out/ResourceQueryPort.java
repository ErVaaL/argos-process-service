package com.erval.argos.process.application.port.out;

/**
 * Port for querying resource service data needed by process jobs.
 */
public interface ResourceQueryPort {
    /**
     * Retrieves device metadata by id.
     *
     * @param deviceId device identifier
     * @return device information and a found flag
     */
    DeviceInfo getDevice(String deviceId);

    /**
     * Lightweight device data needed for job creation validation.
     *
     * @param id     device identifier
     * @param name   device name
     * @param active whether the device is active
     * @param found  whether the device exists
     */
    record DeviceInfo(String id, String name, boolean active, boolean found) {
    }
}
