package com.example.aisupport.exception;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(String message)
    {
        super(message);
    }
}
