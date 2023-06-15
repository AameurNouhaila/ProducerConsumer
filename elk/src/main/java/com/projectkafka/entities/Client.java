package com.projectkafka.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "la-poste")
public class Client {
    @Id
    private String _id;
    private String client_id;
    private String nom;
    private String prenom;
    private String cin;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime date_naissance;
    private String adresse;

    private String evenement_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime date_event;
    private Boolean extract;

    private String pli_id;
    private double poids;
    private double longueur;
    private double largeur;
    private NiveauRecommandation niveau_recommandation;
    private double montant;

    private String offre_id;
    private int codeOffre;
    private int dtyOffre;
    private String description;

    private String partenaire_id;
    private String partenaire_nom;
    private String partenaire_description;
}
