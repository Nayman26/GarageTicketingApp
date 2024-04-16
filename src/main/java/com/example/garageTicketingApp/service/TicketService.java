package com.example.garageTicketingApp.service;

import com.example.garageTicketingApp.dto.VehicleDTO;

public interface TicketService {
    public void leavefromGarage(String plate);
    public String parkToGarage(VehicleDTO vehicleDTO);
    public String getStatus();
}
