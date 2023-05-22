package com.projectkafka.entity;

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
    @ManyToOne
    private Evenement evenement;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Offre offre;
}
