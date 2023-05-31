package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int codeOffre;
    private int dtyOffre;
    private String description;
    @JsonBackReference
    @OneToMany(mappedBy = "offre", cascade = {CascadeType.ALL})
    private List<Pli> plis;
    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.ALL})
    private Partenaire partenaire;
}
