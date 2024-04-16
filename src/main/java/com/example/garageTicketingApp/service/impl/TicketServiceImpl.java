package com.example.garageTicketingApp.service.impl;

import com.example.garageTicketingApp.dto.VehicleDTO;
import com.example.garageTicketingApp.enums.VehicleType;
import com.example.garageTicketingApp.exception.GarageTicketingException;
import com.example.garageTicketingApp.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {
    private static final int MAX_SLOTS = 10;
    private final int[] garageSlots = new int[MAX_SLOTS];
    private final Map<String, VehicleDTO> ticketMap = new HashMap<>();

    @Override
    public String parkToGarage(VehicleDTO vehicleDTO) {
        if (ticketMap.containsKey(vehicleDTO.getPlate()))
            throw new GarageTicketingException(vehicleDTO.getPlate() + " has already parked");

        int requiredSlots = getRequiredSlots(vehicleDTO.getType());
        List<Integer> vehicleSlots = allocateSlot(requiredSlots);
        vehicleDTO.setSlots(vehicleSlots);
        if (vehicleSlots == null) {
            log.warn("Garage is full.");
            throw new GarageTicketingException("Garage is full.");
        }

        ticketMap.putIfAbsent(vehicleDTO.getPlate(), vehicleDTO);
        return "Allocated " + requiredSlots + " slot(s).";
    }

    @Override
    public void leavefromGarage(String plate) {
        if (!ticketMap.containsKey(plate))
            throw new IllegalArgumentException("Invalid ticket or vehicle not found.");

        freeSlot(plate);
        log.info(plate + " has left");
    }

    @Override
    public String getStatus() {
        StringBuilder status = new StringBuilder("Status:\n");
        for (Map.Entry<String, VehicleDTO> entry : ticketMap.entrySet()) {
            status.append(entry.getValue().getPlate()).append(" ")
                    .append(entry.getValue().getColor()).append(" ")
                    .append(entry.getValue().getType()).append(" ")
                    .append(entry.getValue().getSlots()).append("\n");
        }
        status.append("Garage Slots: ").append(Arrays.toString(garageSlots));
        return status.toString();
    }

    private List<Integer> allocateSlot(int requiredSlots) {
        int startIndex = -1;
        for (int i = 0; i < garageSlots.length; i++) {
            if (garageSlots[i] == 0) {
                if (startIndex == -1) {
                    startIndex = i;
                }
                if (i - startIndex == requiredSlots) {
                    List<Integer> vehicleSlots = new ArrayList<>();
                    for (int j = startIndex; j < i; j++) {
                        garageSlots[j] = 1;
                        vehicleSlots.add(j + 1); // +1 for start index of from 1 instead of 0
                    }
                    garageSlots[i] = 1;
                    return vehicleSlots;
                }
            }
        }
        return null;
    }

    private void freeSlot(String plate) {
        var slots = ticketMap.get(plate).getSlots();
        int firsSlot = slots.get(0); // -1 for get start index of slots in array
        for (int i = firsSlot - 1; i < slots.size() + 1; i++) { // +1 for next slot near the vehicle
            garageSlots[i] = 0;
        }
        ticketMap.remove(plate);
    }

    private int getRequiredSlots(VehicleType type) {
        return switch (type) {
            case Car -> 1;
            case Suv -> 2;
            case Truck -> 4;
        };
    }
}