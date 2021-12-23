package com.example.Flight.service;


import com.example.Flight.model.Flight;
import com.example.Flight.model.Ticket;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class DateService {
    private TreeMap<LocalDate, List<Flight>> dateList = new TreeMap<>();

    public Boolean saveDate(String date1, Flight flight){
        LocalDate date = convertStringToTimestamp(date1);
        if(dateList.containsKey(date)){
            List<Flight> flightsList = dateList.get(date);
            flightsList.add(flight);
            dateList.replace(date, flightsList);
            return true;
        }
        List<Flight> flights = new ArrayList<>();
        flights.add(flight);
        dateList.put(date, flights);
        return true;
    }

    public Boolean updateFlightsUsingDate(Ticket ticket, String oldDate, Flight flight){
        LocalDate date = convertStringToTimestamp(oldDate);
        List<Flight> flights=dateList.get(date);
        flights.remove(flight);
        dateList.replace(date, flights);
        LocalDate new_date = convertStringToTimestamp(flight.getFlightDate());
        List<Flight> flightsTemp=dateList.get(new_date);
        if(flightsTemp==null){
            List<Flight> fligh = new ArrayList<>();
            fligh.add(flight);
            dateList.put(new_date,fligh);
            return true;
        }
        flightsTemp.add(flight);
        dateList.put(new_date,flightsTemp);
        return true;
    }


    public Map<LocalDate, List<Flight>> getFlightByDateRange(String startDate1, String endDate1){
        LocalDate startDate = convertStringToTimestamp(startDate1);
        LocalDate endDate =   convertStringToTimestamp(endDate1);
        return  dateList.subMap(startDate, endDate.plusDays(1));

    }

    public static LocalDate convertStringToTimestamp(String strDate) {
        return Optional.ofNullable(strDate) // wrap the String into an Optional
                .map(str -> LocalDate.parse(str)) // convert into a LocalDate and fix the hour:minute:sec to 00:00:00
                .orElse(null);
    }
}
