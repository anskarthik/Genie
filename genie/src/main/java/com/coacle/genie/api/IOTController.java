package com.coacle.genie.api;

import com.coacle.genie.service.IOTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iot")
public class IOTController {

    @Autowired
    private IOTService iotService;

    @PostMapping("/{deviceMacId}")
    public void publishMessage(@RequestBody String message,
                               @PathVariable("deviceMacId") String deviceMacId) {
        iotService.publishMessage(message, deviceMacId);
    }

}
