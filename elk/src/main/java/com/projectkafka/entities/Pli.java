package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pli {
    @Id
    private Long id;
    private double poids;
    private double longueur;
    private double largeur;
    private NiveauRecommandation niveauRecommandation;
    private double montant;
    @JsonBackReference
    private Evenement evenement;
    @JsonManagedReference
    private Offre offre;
}
