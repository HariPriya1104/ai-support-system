package com.example.aisupport.service;

import com.example.aisupport.ai.AiService;
import com.example.aisupport.dto.TicketDTO;
import com.example.aisupport.dto.TicketResponseDTO;
import com.example.aisupport.dto.UserDTO;
import com.example.aisupport.exception.TicketNotFoundException;
import com.example.aisupport.kafka.TicketKafkaProducer;
import com.example.aisupport.model.Ticket;
import com.example.aisupport.model.User;
import com.example.aisupport.repository.TicketRepository;
import com.example.aisupport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketKafkaProducer ticketKafkaProducer;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AiService aiService;
    public String createTicket(Ticket ticket){
        ticketRepository.save(ticket);
        return "Ticket created successfully";
    }

    public Ticket getTicketById(Integer id)
    {
        return ticketRepository.findById(id).orElseThrow(()-> new TicketNotFoundException("ticket not found"));
    }

    public List<Ticket> getByStatus(String status)
    {
        return ticketRepository.findTicketByStatusIgnoreCase(status);
    }

    public List<Ticket> getByTitle(String keyword)
    {
        return ticketRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Page<TicketResponseDTO> getTickets(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findAll(pageable).map(this::convertToDTO);
    }

    public TicketResponseDTO convertToDTO(Ticket ticket)
    {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setPriority(ticket.getPriority());
        dto.setId(ticket.getId());
        dto.setStatus(ticket.getStatus());
        dto.setTitle(ticket.getTitle());
        dto.setCreatedBy(ticket.getCreatedBy());
        dto.setDescription(ticket.getDescription());
        if(ticket.getUser() != null)
        {
            try {
                UserDTO user = restTemplate.getForObject("http://localhost:8082/users/" + ticket.getUser().getId(), UserDTO.class);
                if(user != null)
                {
                    dto.setAssignedUserName(user.getName());
                    dto.setAssignedUserEmail(user.getEmail());
                }
            }
            catch (Exception e)
            {
                dto.setAssignedUserName("Unknown");
            }
        }
        return dto;
    }
    public Ticket saveTicket(TicketDTO dto)
    {
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setStatus(dto.getStatus());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());
        ticket.setCreatedBy(dto.getCreatedBy());
        if(dto.getUserId() != null)
        {
            User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("user not found"));
            ticket.setUser(user);
        }
        ticketKafkaProducer.sendMessage("test-topic","Ticket created : " + ticket.getTitle());

        String aiReply = aiService.generateReply(dto.getTitle(),dto.getDescription());
        System.out.println("AI Reply : " + aiReply);
        return ticketRepository.save(ticket);
    }
    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    public String updateTicket(Integer id, TicketDTO dto){
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException("Ticket Not Found"));

        if(ticket == null){
            return "Ticket not found";
        }

        ticket.setTitle(dto.getTitle());
        ticket.setStatus(dto.getStatus());
        ticket.setPriority(dto.getPriority());
        ticket.setCreatedBy(dto.getCreatedBy());

        ticketRepository.save(ticket);
        return "ticket updated successfully";
    }

    public String deleteTicket(Integer id){
        ticketRepository.deleteById(id);
        return "Ticket deleted successfully";
    }
}
