package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Integer> date_naissance;
    private String adresse;

    private String evenement_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Integer> date_event;
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
