package com.example.Flight;

import com.example.Flight.model.Event;
import com.example.Flight.model.MyModel;
import com.example.Flight.service.DateService;
import com.example.Flight.service.FlightService;
import com.example.Flight.service.TicketService;
import com.github.javafaker.Faker;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class FlightControllerTest {
    @Autowired
   private FlightController flightController;
   private FlightService flightService = mock(FlightService.class);
   private DateService dateService = mock(DateService.class);
   private TicketService ticketService = mock(TicketService.class);
   private List<Event> eventList = new ArrayList<>();

    @Rule
    public ExpectedException exceptions = ExpectedException.none();

    @Spy
    private FlightController spyFlight;

   @BeforeEach
   public void beforeEach() {
       this.flightController = new FlightController();
       TestUtil.InjectObjects(this.flightController, "flightService", flightService);
       TestUtil.InjectObjects(this.flightController, "dateService", dateService);
       TestUtil.InjectObjects(this.flightController, "ticketService", ticketService);
       eventList.clear();
   }

   @Test
   public void testSuccuessful(){
       HashMap<String, String> map = new HashMap<>();
       map.put("Status", "success");
       when(flightService.addFlight(any())).thenReturn(true);
       when(ticketService.addId(any())).thenReturn(true);
       MyModel model = new MyModel();
       model.setEvent(event().get(0));
       assertThat(flightController.postTicket(model).getBody()).isEqualTo(map);

   }
     @Test
     public void testDuplicateTicketId(){
         when(flightService.addFlight(any())).thenReturn(true);
         MyModel model = new MyModel();
         model.setEvent(duplicatEvent().get(0));
         flightController.postTicket(model);
         try {
             flightController.postTicket(model);
         }
         catch (Exception ex){
             assertEquals(ex.getMessage(), "ticketId already exists");
         }
     }
     @Test
     public void testInvalidDate(){
         MyModel model = new MyModel();
         duplicatEvent().get(0).setFlightDate("12/1/12");
         model.setEvent(duplicatEvent().get(0));
         try {
             flightController.postTicket(model);
         }
         catch (Exception ex){
             assertEquals(ex.getMessage(), "Date is Invalid");
         }
     }


   @Test
   public void testDuplicateSeatNUmber(){
       MyModel model = new MyModel();
       model.setEvent(duplicatEvent().get(0));
       try {
           flightController.postTicket(model);
       }
       catch (Exception ex){
           assertEquals(ex.getMessage(), "seatNumber already taken");
       }
   }
    @Test
    public  void testInvalidStartDay(){
        try {
            flightController.getTicket("12/09/2020", "12/12/2021");
        }
        catch (Exception ex){
            assertEquals(ex.getMessage(), "startDate format is invalid");
        }
    }

    @Test
    public  void testInvalidEndDay(){
        try {
            flightController.getTicket("2021-12-12", "12/09/2020");
        }
        catch (Exception ex){
            assertEquals(ex.getMessage(), "endDate format is invalid");
        }
    }

    @Test
    public void testForProperParamValidation() throws ParseException {
        try {
            flightController.getTicket("2021-12-09", "2020-12-09");
        }
        catch (Exception ex){
            assertEquals("endDate cannot be before startDate", ex.getMessage());
        }
    }

    @Test
    public void testForNullStartDate(){
        try {
            flightController.getTicket( "","2020-12-09");
        }
        catch (Exception ex){
            assertEquals("startDate is empty", ex.getMessage());
        }
    }

    @Test
    public void testForNullEndDate(){
        try {
            flightController.getTicket( "2020-12-09","");
        }
        catch (Exception ex){
            assertEquals("endDate is empty", ex.getMessage());
        }
    }

    @Test
    public void testSuccessTicketResponse() throws ParseException {
        assertThat(flightController.getTicket( "2020-12-09","2021-12-09").getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    public List<Event> event(){
     Faker faker = new Faker();
     for(int i =0; i<4; i++){
         int value = (int) Math.random()*5;
         eventList.add(
                 new Event(
                         i,
                         dateList().get(i).toString(),
                                 flightName().get(i),
                                 seatNumber().get(i),
                                 faker.number().numberBetween(123443, 78432)));
     }
     return eventList;
    }

    public List<Event> duplicatEvent(){
        Faker faker = new Faker();
        for(int i =0; i<4; i++){
            int value = (int) Math.random()*5;
            eventList.add(
                    new Event(
                            1,
                            dateList().get(i).toString(),
                            flightName().get(i),
                            seatNumber().get(i),
                            faker.number().numberBetween(123443, 78432)));
        }
        return eventList;
    }

    public List<String> flightName(){
       List<String> fName = new ArrayList<>();
       fName.add("AF1");
       fName.add("AF2");
       fName.add("AB5");
       fName.add("AC7");
       fName.add("AE9");
       return fName;
    }

    public List<String> seatNumber(){
        List<String> fName = new ArrayList<>();
        fName.add("F1");
        fName.add("F2");
        fName.add("B5");
        fName.add("C7");
        fName.add("E9");

        return fName;
    }

    public List<String> dateList(){
       List<String> date = new ArrayList<>();
       date.add("2021/12/01");
       date.add("2021/12/02");
       date.add("2021/12/03");
       date.add("2021/12/04");
       return date;
    }



}
