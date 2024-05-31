package org.example.acmc.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcStatus {
    private float temperature;
    private float humidity;
    private String acState;
}
