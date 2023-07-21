package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date_naissance;
    private String adresse;

    private String evenement_id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date_event;
    private Boolean extract;

    private String pli_id;
    private double poids;
    private double longueur;
    private double largeur;
    private NiveauRecommandation niveau_recommandation;
    private double montant;

    private String offre_id;
    private String codeOffre;
    private int dtyOffre;
    private String description;

    private String partenaire_id;
    private String partenaire_nom;
    private String partenaire_description;

    public String getDateEventFormatted() {
        return date_event.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
