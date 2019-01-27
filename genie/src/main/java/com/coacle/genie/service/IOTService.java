package com.coacle.genie.service;

import com.coacle.genie.messaging.AmqpProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IOTService {

    @Autowired
    AmqpProducer producer;

    public void publishMessage(String message, String deviceMacId) {
        producer.produce(message, "device." + deviceMacId);
    }

}
