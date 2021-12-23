package com.example.Flight;

import com.example.Flight.Exception.ApiRequestException;
import com.example.Flight.model.*;
import com.example.Flight.service.DateService;
import com.example.Flight.service.FlightService;
import com.example.Flight.service.TicketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private DateService dateService;

    @PostMapping("/api/tickets")
    public ResponseEntity<HashMap<String, String>> postTicket(@RequestBody MyModel eventRequest) {
        HashMap<String, String> map = new HashMap<>();
        Optional optional = Optional.of(eventRequest);
        if(optional.isEmpty()){
            throw new ApiRequestException("Cannot be null");
        }

        MyModel model = (MyModel) optional.get();
        Event event = model.getEvent();

        basicFlightChecks(event);
        dateLengthCheck(event.getFlightDate());
        if(!flightService.addFlight(new Ticket(event.getFlightNumber(),
                        event.getSeatNumber(),
                        event.getTicketCost(),
                        event.getFlightNumber(),
                        event.getFlightDate()))) {

            throw new ApiRequestException("seatNumber already taken");
        }
        else{
            ticketService.addId(event.getTicketId()+"");
        }

        map.put("Status", "success");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @GetMapping("/api/flights")
    public ResponseEntity<DateListFlight> getTicket(@RequestParam(value = "startDate", required = true) String startDate,
                                           @RequestParam(value = "endDate", required = true) String endDate) throws ParseException {
        checkNullDate(startDate, endDate);
        dateValidCheck(startDate, endDate);
        dateLengthCheck(startDate, endDate);
        if(compareDate(startDate, endDate)){
            throw new ApiRequestException("endDate cannot be before startDate");
        }
        List<Dates> dateList = new ArrayList<>();
        Map<LocalDate, List<Flight>>myLists= dateService.getFlightByDateRange(startDate, endDate);
        for(LocalDate date: myLists.keySet()){
            Dates myDate = new Dates();
            myDate.setDate(date+"");
            myDate.setFlights(myLists.get(date));
            dateList.add(myDate);
        }

        return new ResponseEntity<>(new DateListFlight(dateList),HttpStatus.OK);

    }

    protected void dateValidCheck(@RequestParam(value = "startDate", required = true) String startDate, @RequestParam(value = "endDate", required = true) String endDate) {
        if(!isValidFormat(startDate)){
            throw new ApiRequestException("startDate format is invalid");
        }
        if(!isValidFormat(endDate)){
            throw new ApiRequestException("endDate format is invalid");
        }
    }

    private void checkNullDate(String startDate, String endDate) {
        if(startDate.isEmpty()){
            throw new ApiRequestException("startDate is empty");
        }
        if (endDate.isEmpty()){
            throw new ApiRequestException("endDate is empty");
        }
    }

    protected void basicFlightChecks(Event event) {
        if(ticketService.checkTicketId(""+event.getTicketId())){
            throw new ApiRequestException("ticketId already exists");
        }

        if(!StringUtils.isAlphanumeric(event.getSeatNumber())){
            throw new ApiRequestException("Seat Number should be Alphanumeric");
        }

        if(!StringUtils.isAlphanumeric(event.getFlightNumber())){
            throw new ApiRequestException("Flight Number should be Alphanumeric");
        }
    }

    protected void dateLengthCheck(String date){
        if(date.length()!=10){
            throw new ApiRequestException("Date is Invalid");
        }
    }

    protected void dateLengthCheck(String startDate, String endDate){
        if(startDate.length()!=10){
            throw new ApiRequestException("Start Date is Invalid");
        }

        if(endDate.length()!=10){
            throw new ApiRequestException("End Date is Invalid");
        }

    }

    public static boolean isValidFormat(String format) {
        if (format.trim().equals(""))
        {
            return true;
        }
        else
        {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
            sdfrmt.setLenient(false);
            try
            {
                Date javaDate = sdfrmt.parse(format);
            }
            catch (ParseException e)
            {
                return false;
            }
            return true;
        }
    }

    public boolean compareDate(String startDate, String endDate) throws  ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);

        if (date1.after(date2)) {
            return  true;
        }
        return false;

    }

}
