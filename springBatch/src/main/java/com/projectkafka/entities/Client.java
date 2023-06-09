package com.projectkafka.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Document(indexName = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private String cin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_naissance")
    private Date dateNaissance;
    private String adresse;
    @JsonManagedReference
    @OneToMany(mappedBy = "client", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Evenement> evenement;
}
