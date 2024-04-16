package com.example.garageTicketingApp.api.request;

import com.example.garageTicketingApp.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ParkRequest {
    String plate;
    String color;
    VehicleType type;
}
