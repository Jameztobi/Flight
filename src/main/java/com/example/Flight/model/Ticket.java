package com.example.Flight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String ticketId;
    private String seatNumber;
    private Integer ticketCost;
    private String flightNumber;
    private String flightDate;
}
