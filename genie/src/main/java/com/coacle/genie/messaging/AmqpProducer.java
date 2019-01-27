package com.coacle.genie.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmqpProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${amqp.rabbitmq.exchange}")
    private String exchange;

    public void produce(String msg, String routingKey) {
        amqpTemplate.convertAndSend(exchange, routingKey, msg);
        System.out.println("Send msg = " + msg);
    }
}
