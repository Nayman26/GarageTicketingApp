package com.example.garageTicketingApp.api.controller;

import com.example.garageTicketingApp.service.TicketService;
import com.example.garageTicketingApp.api.request.ParkRequest;
import com.example.garageTicketingApp.dto.VehicleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GarageController {

    private final TicketService ticketService;

    private GarageController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/park")
    public ResponseEntity<String> park(@RequestBody ParkRequest request) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setPlate(request.getPlate());
        vehicleDTO.setColor(request.getColor());
        vehicleDTO.setType(request.getType());

        String response = ticketService.parkToGarage(vehicleDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/leave/{plate}")
    public ResponseEntity<String> leave(@PathVariable("plate") String plate) {
        try {
            ticketService.leavefromGarage(plate);
            return ResponseEntity.ok(plate + " has left");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ticket or vehicle not found.");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        String status = ticketService.getStatus();
        return ResponseEntity.ok(status);
    }
}
