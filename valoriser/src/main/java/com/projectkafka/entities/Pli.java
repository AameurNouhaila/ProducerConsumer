package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Pli {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double poids;
    private double longueur;
    private double largeur;
    @Enumerated(EnumType.STRING)
    private NiveauRecommandation niveauRecommandation;
    private double montant;
    @JsonBackReference
    @ManyToOne
    private Evenement evenement;
    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.ALL})
    private Offre offre;
}
