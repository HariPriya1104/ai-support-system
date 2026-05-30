package com.example.aisupport.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {
    private int id;
    private String title;
    private String status;
    private String priority;
    private String description;
    private String createdBy;
    private String assignedUserName;
    private String assignedUserEmail;

}
