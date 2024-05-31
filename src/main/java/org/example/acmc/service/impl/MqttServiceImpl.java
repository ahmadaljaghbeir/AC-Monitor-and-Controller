package org.example.acmc.service.impl;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.*;

import org.example.acmc.model.AcStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttServiceImpl {
    private final MqttClient mqttClient;
    private final String[] subscribeTopics;
    private final String publishTopic;
    @Getter
    private AcStatus acStatus;

    public MqttServiceImpl(@Value("${mqtt.broker.url}") String brokerUrl,
                           @Value("${mqtt.topic.subscribe}") String[] subscribeTopics,
                           @Value("${mqtt.topic.publish}") String publishTopic) throws MqttException {
        this.mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId());
        this.subscribeTopics = subscribeTopics;
        this.publishTopic = publishTopic;
        this.acStatus = new AcStatus();  // Ensure acStatus is initialized

        connectAndSubscribe();
    }

    private void connectAndSubscribe() throws MqttException {
        mqttClient.connect();
        for (String topic : subscribeTopics) {
            mqttClient.subscribe(topic, (t, message) -> handleIncomingMessage(t, new String(message.getPayload())));
        }
    }

    private void handleIncomingMessage(String topic, String message) {
        // Parse the message and update acStatus
        // Expected format: "Temperature: 25.5 C, Humidity: 60.0%"
        if (topic.equals(subscribeTopics[1])) {
            System.out.println("1. " + message);
            String[] parts = message.split(",");
            float temperature = Float.parseFloat(parts[0].split(":")[1].trim().split(" ")[0]);
            float humidity = Float.parseFloat(parts[1].split(":")[1].trim().split("%")[0]);

            acStatus.setTemperature(temperature);
            acStatus.setHumidity(humidity);
        }
        if (topic.equals(subscribeTopics[0])) {
            System.out.println("2. " +message);
            acStatus.setAcState(message);
        }
    }

    public void sendAcCommand(String command) throws MqttException {
        if (!mqttClient.isConnected()) {
            connectAndSubscribe();
        }
        MqttMessage message = new MqttMessage(command.getBytes());
        mqttClient.publish(publishTopic, message);
    }
}
