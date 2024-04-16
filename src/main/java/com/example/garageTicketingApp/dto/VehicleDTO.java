package com.example.garageTicketingApp.dto;

import com.example.garageTicketingApp.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleDTO {
    String plate;
    String color;
    VehicleType type;
    List<Integer> slots;

    public VehicleDTO() {}

    public VehicleDTO(String plate, String color, VehicleType type) {
        this.plate = plate;
        this.color = color;
        this.type = type;
    }
}