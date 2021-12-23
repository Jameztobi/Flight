package com.example.Flight.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class TicketService {
    private ArrayList<String> ticketList = new ArrayList<>();

    public boolean addId(String id){
        return ticketList.add(id);
    }

    public boolean checkTicketId(String id){
        if(ticketList.contains(id)){
            return true;
        }
        return false;
    }



}
