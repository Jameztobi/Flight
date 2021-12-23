package com.example.Flight.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
   private Integer ticketId;
   private String flightDate;
   private String flightNumber;
   private String seatNumber;
   private Integer ticketCost;

}
