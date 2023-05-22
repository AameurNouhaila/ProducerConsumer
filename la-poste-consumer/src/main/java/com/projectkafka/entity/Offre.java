package com.projectkafka.entity;

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
    @OneToMany(mappedBy = "offre", cascade = {CascadeType.ALL})
    private List<Pli> plis;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Partenaire partenaire;
}
