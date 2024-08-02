package com.chatApp.ChatApplication.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupOfUser {

    Boolean isPublic = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    @ManyToOne
    @JsonBackReference
    private User admin;
    @ManyToMany(mappedBy = "groups")
    @JsonBackReference
    private Set<User> users;

}
