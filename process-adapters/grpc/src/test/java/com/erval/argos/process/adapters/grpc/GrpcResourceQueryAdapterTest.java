package com.erval.argos.process.adapters.grpc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.erval.argos.contracts.resource.v1.Device;
import com.erval.argos.contracts.resource.v1.GetDeviceRequest;
import com.erval.argos.contracts.resource.v1.GetDeviceResponse;
import com.erval.argos.contracts.resource.v1.ResourceQueryServiceGrpc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GrpcResourceQueryAdapterTest {

    @Mock
    private ResourceQueryServiceGrpc.ResourceQueryServiceBlockingStub stub;

    @Test
    void getDevice_returnsNotFound() {
        when(stub.getDevice(GetDeviceRequest.newBuilder().setDeviceId("dev-404").build()))
                .thenReturn(GetDeviceResponse.newBuilder().setFound(false).build());

        var adapter = new GrpcResourceQueryAdapter(stub);
        var info = adapter.getDevice("dev-404");

        assertFalse(info.found());
        assertFalse(info.active());
    }

    @Test
    void getDevice_mapsFoundDevice() {
        var device = Device.newBuilder()
                .setId("dev-1")
                .setName("Device One")
                .setActive(true)
                .build();
        when(stub.getDevice(GetDeviceRequest.newBuilder().setDeviceId("dev-1").build()))
                .thenReturn(GetDeviceResponse.newBuilder().setFound(true).setDevice(device).build());

        var adapter = new GrpcResourceQueryAdapter(stub);
        var info = adapter.getDevice("dev-1");

        assertTrue(info.found());
        assertTrue(info.active());
    }
}
