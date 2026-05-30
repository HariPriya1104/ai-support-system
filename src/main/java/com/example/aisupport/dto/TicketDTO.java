package com.example.aisupport.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "status cannot be empty")
    private String status;

    @NotBlank(message = "priority cannot be empty")
    private String priority;

    @NotBlank(message = "createdby cannot be empty")
    private String createdBy;

    private String description;

    private Integer userId;
}
