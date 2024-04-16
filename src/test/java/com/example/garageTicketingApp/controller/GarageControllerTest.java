package com.example.garageTicketingApp.controller;
import com.example.garageTicketingApp.api.controller.GarageController;
import com.example.garageTicketingApp.api.request.ParkRequest;
import com.example.garageTicketingApp.dto.VehicleDTO;
import com.example.garageTicketingApp.enums.VehicleType;
import com.example.garageTicketingApp.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GarageControllerTest {

    @Mock
    private TicketService ticketService;
    @InjectMocks
    private GarageController controller;

    @Test
    public void testPark() {
        ParkRequest request = Mockito.mock(ParkRequest.class);
        Mockito.when(request.getType()).thenReturn(VehicleType.Car);

        when(ticketService.parkToGarage(any(VehicleDTO.class))).thenReturn("Some response");
        ResponseEntity<String> response = controller.park(request);

        assertEquals("Some response", response.getBody());
        verify(ticketService, times(1)).parkToGarage(any(VehicleDTO.class));
    }

    @Test
    public void testLeave() {
        String plate = "plate";
        ResponseEntity<String> response = controller.leave(plate);
        assertEquals(plate + " has left", response.getBody());
        verify(ticketService, times(1)).leavefromGarage(plate);
    }

    @Test
    public void testLeave_InvalidPlate() {
        String plate = "invalid_plate";
        doThrow(new IllegalArgumentException()).when(ticketService).leavefromGarage(plate);
        ResponseEntity<String> response = controller.leave(plate);
        assertEquals("Invalid ticket or vehicle not found.", response.getBody());
    }

    @Test
    public void testStatus() {
        when(ticketService.getStatus()).thenReturn("Some status");
        ResponseEntity<String> response = controller.status();
        assertEquals("Some status", response.getBody());
    }

    @Test
    public void testLeavefromGarage_Exception() {
        String plate = "plate";
        doThrow(new IllegalArgumentException()).when(ticketService).leavefromGarage(plate);
        ResponseEntity<String> response = controller.leave(plate);
        assertEquals("Invalid ticket or vehicle not found.", response.getBody());
    }
}
