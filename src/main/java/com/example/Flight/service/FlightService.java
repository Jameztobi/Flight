package com.example.Flight.service;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class FlightService {
    @Autowired
    private DateService dateService;

    private HashMap<String, Flight> flightList = new HashMap<>();
    private String oldDate = "";


    public Boolean addFlight(Ticket ticket) {
        if (flightList.containsKey(ticket.getFlightNumber())) {
            if(addSeat(ticket)){
                dateService.updateFlightsUsingDate(ticket, oldDate, flightList.get(ticket.getFlightNumber()));
                return true;
            }
            return  false;

        }
        addNewFlight(ticket);
        return true;
    }

    private Boolean addSeat(Ticket ticket) {
        Flight flight = flightList.get(ticket.getFlightNumber());
        oldDate = flight.getFlightDate();
        if (!flight.getFlightDate().equals(ticket.getFlightDate())) {
            if (flight.getOccupiedSeats().contains(ticket.getSeatNumber())) {
                return false;
            }
            flight.getOccupiedSeats().add(ticket.getSeatNumber());
            flight.setRevenue(flight.getRevenue() + ticket.getTicketCost());
            flight.setFlightDate(ticket.getFlightDate());
            return true;
        }
        if (flight.getOccupiedSeats().contains(ticket.getSeatNumber())) {
            return false;
        }
        flight.getOccupiedSeats().add(ticket.getSeatNumber());
        flight.setRevenue(flight.getRevenue() + ticket.getTicketCost());
        return true;
    }

    private void addNewFlight(Ticket ticket) {
        ArrayList<String> seat = new ArrayList<>();
        seat.add(ticket.getSeatNumber());
        Flight flight = new Flight(ticket.getFlightNumber(), ticket.getFlightDate(), ticket.getTicketCost(), seat);
        flightList.put(ticket.getFlightNumber(), flight);
        dateService.saveDate(ticket.getFlightDate(), flight);
    }



}
