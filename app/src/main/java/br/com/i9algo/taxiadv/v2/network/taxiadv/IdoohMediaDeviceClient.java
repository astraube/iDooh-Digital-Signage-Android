package br.com.i9algo.taxiadv.v2.network.taxiadv;

import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.mobileconnectors.apigateway.annotation.Operation;
import com.amazonaws.mobileconnectors.apigateway.annotation.Parameter;
import com.amazonaws.mobileconnectors.apigateway.annotation.Service;

import br.com.i9algo.taxiadv.domain.models.Device;


@Service(endpoint = "https://ptvge2zpz3.execute-api.us-east-1.amazonaws.com/p")
public interface IdoohMediaDeviceClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);

    /**
     *
     *
     * @param body
     * @return void
     */
    @Operation(path = "/device/geo", method = "POST")
    void deviceGeoPost(Device body);

    /**
     *
     *
     * @param deviceSerial
     * @param deviceMac
     * @return void
     */
    @Operation(path = "/device/log", method = "POST")
    void deviceLogPost(
            @Parameter(name = "device_serial", location = "query") String deviceSerial,
            @Parameter(name = "device_mac", location = "query") String deviceMac);

    /**
     *
     *
     * @return void
     */
    @Operation(path = "/device/log-segment", method = "POST")
    void deviceLogSegmentPost();

}

