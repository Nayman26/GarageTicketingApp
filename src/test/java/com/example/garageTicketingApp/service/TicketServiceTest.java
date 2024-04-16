package com.example.garageTicketingApp.service;

import com.example.garageTicketingApp.dto.VehicleDTO;
import com.example.garageTicketingApp.enums.VehicleType;
import com.example.garageTicketingApp.exception.GarageTicketingException;
import com.example.garageTicketingApp.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void testParkToGarage() {
        VehicleDTO vehicle = Mockito.mock(VehicleDTO.class);
        Mockito.when(vehicle.getType()).thenReturn(VehicleType.Car);
        String result = ticketService.parkToGarage(vehicle);
        assertEquals("Allocated 1 slot(s).", result);
    }

    @Test
    public void testLeaveFromGarage() {
        VehicleDTO vehicle = new VehicleDTO("ABC123", "Red", VehicleType.Car);
        ticketService.parkToGarage(vehicle);
        assertDoesNotThrow(() -> ticketService.leavefromGarage("ABC123"));
    }

    @Test
    public void testGetStatus() {
        VehicleDTO vehicle = new VehicleDTO("ABC123", "Red", VehicleType.Car);
        ticketService.parkToGarage(vehicle);
        String status = ticketService.getStatus();
        assertTrue(status.contains("ABC123 Red Car"));
    }

    @Test
    void parkToGarage_FullGarage() {
        // Fill the garage
        for (int i = 0; i < 2; i++) {
            VehicleDTO vehicle = new VehicleDTO("ABC"+i, "Red", VehicleType.Truck);
            ticketService.parkToGarage(vehicle);
        }

        // Create a mock VehicleDTO for getting exception
        VehicleDTO vehicleDTO = Mockito.mock(VehicleDTO.class);
        Mockito.when(vehicleDTO.getType()).thenReturn(VehicleType.Car);

        GarageTicketingException exception = assertThrows(GarageTicketingException.class, () -> ticketService.parkToGarage(vehicleDTO));
        assertEquals("Garage is full.", exception.getMessage());
    }

    @Test
    void parkToGarage_AlreadyParked() {
        VehicleDTO vehicleDTO = Mockito.mock(VehicleDTO.class);
        Mockito.when(vehicleDTO.getType()).thenReturn(VehicleType.Car);
        Mockito.when(vehicleDTO.getPlate()).thenReturn("ABC123");

        ticketService.parkToGarage(vehicleDTO);
        GarageTicketingException exception = assertThrows(GarageTicketingException.class, () -> ticketService.parkToGarage(vehicleDTO));
        assertEquals("ABC123 has already parked", exception.getMessage());
    }

    @Test
    public void testLeaveInvalidVehicle() {
        assertThrows(IllegalArgumentException.class, () -> ticketService.leavefromGarage("INVALID_PLATE"));
    }

    @Test
    public void testGetStatusEmptyGarage() {
        String status = ticketService.getStatus();
        assertEquals("Status:\nGarage Slots: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]", status);
    }
}
