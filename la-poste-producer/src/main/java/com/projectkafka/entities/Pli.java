package com.projectkafka.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pli {
    private Long id;
    private double poids;
    private double longueur;
    private double largeur;
    private NiveauRecommandation niveauRecommandation;
    private double montant;
    private Offre offre;
}
