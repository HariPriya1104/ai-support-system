package com.example.aisupport.controller;

import com.example.aisupport.dto.TicketDTO;
import com.example.aisupport.dto.TicketResponseDTO;
import com.example.aisupport.model.Ticket;
import com.example.aisupport.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    @Autowired
    public TicketService ticketService;

    @PostMapping("/ticket")
    public Ticket createTicket(@Valid @RequestBody TicketDTO dto)
    {
        return ticketService.saveTicket(dto);
    }

    @GetMapping("/tickets")
    public Page<TicketResponseDTO> getTickets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size)
    {
        return ticketService.getTickets(page, size);
    }

    @GetMapping("/ticket")
    public List<Ticket> getAllTicket(){
        return ticketService.getAllTickets();
    }

    @PutMapping("/ticket/{id}")
    public String updateTicket(@PathVariable Integer id,@RequestBody TicketDTO dto){
        return ticketService.updateTicket(id, dto);
    }

    @DeleteMapping("/ticket/{id}")
    public String deleteTicket(@PathVariable Integer id)
    {
        return ticketService.deleteTicket(id);
    }

    @GetMapping("/ticket/{id}")
    public Ticket getByTicketId(@PathVariable Integer id)
    {
        return ticketService.getTicketById(id);
    }
    @GetMapping("/status/{status}")
    public List<Ticket> getTicketByStatus(@PathVariable String status)
    {
        return ticketService.getByStatus(status);
    }

    @GetMapping("/search")
    public List<Ticket> getByTitle(@RequestParam String keyword)
    {
        return ticketService.getByTitle(keyword);
    }
}
