package org.example.acmc.controller;


import org.eclipse.paho.client.mqttv3.MqttException;

import org.example.acmc.model.AcStatus;
import org.example.acmc.service.impl.MqttServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AcController {
    @Autowired
    private MqttServiceImpl mqttServiceImpl;

    @GetMapping("/receive")
    public String index(Model model) {
        AcStatus acStatus = mqttServiceImpl.getAcStatus();
        if (acStatus == null) {
            acStatus = new AcStatus(); // Provide a default AcStatus if null
            acStatus.setAcState("OFF"); // Set a default value for acState
        }
        model.addAttribute("acStatus", acStatus);
        return "acController";
    }

    @PostMapping("/control")
    public String control(@RequestParam String command, Model model) {
        try {
            System.out.println("Command: " + command);
            mqttServiceImpl.sendAcCommand(command);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return "redirect:/receive";
    }
}
