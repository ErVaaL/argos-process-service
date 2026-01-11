package com.erval.argos.process.adapters.grpc;

import com.erval.argos.contracts.resource.v1.GetDeviceRequest;
import com.erval.argos.contracts.resource.v1.ResourceQueryServiceGrpc;
import com.erval.argos.process.application.port.out.ResourceQueryPort;

import lombok.RequiredArgsConstructor;

/**
 * gRPC adapter that queries the resource service for device metadata.
 */
@RequiredArgsConstructor
public class GrpcResourceQueryAdapter implements ResourceQueryPort {

    private final ResourceQueryServiceGrpc.ResourceQueryServiceBlockingStub stub;

    /**
     * Fetches device data and maps it to a lightweight DTO.
     *
     * @param deviceId device identifier
     * @return device info including a found flag
     */
    @Override
    public ResourceQueryPort.DeviceInfo getDevice(String deviceId) {
        var req = GetDeviceRequest.newBuilder()
                .setDeviceId(deviceId)
                .build();

        var res = stub.getDevice(req);

        if (!res.getFound())
            return new DeviceInfo(null, null, false, false);

        var d = res.getDevice();
        return new DeviceInfo(
                d.getId(),
                d.getName(),
                d.getActive(),
                true);
    }

}
