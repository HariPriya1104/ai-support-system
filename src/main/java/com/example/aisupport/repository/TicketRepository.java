package com.example.aisupport.repository;

import com.example.aisupport.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findTicketByStatusIgnoreCase(String status);

    List<Ticket> findByTitleContainingIgnoreCase(String keyword);

}
