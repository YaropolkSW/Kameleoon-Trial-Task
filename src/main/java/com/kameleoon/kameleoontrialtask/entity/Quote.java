package com.kameleoon.kameleoontrialtask.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quote_table")
public class Quote {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quote")
    private String quote;

    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "votes")
    private int votes;

}
