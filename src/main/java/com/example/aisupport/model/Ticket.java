package com.example.aisupport.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "ticket")
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title cannot be empty")
    public String title;

    public String description;

    @NotBlank(message = "status cannot be empty")
    public String status;

    public String priority;

    public String createdBy;

    public String resolution;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    public User user;
}
