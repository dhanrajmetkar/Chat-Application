package com.chatApp.ChatApplication.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "groups")
    private List<User> users;

}
