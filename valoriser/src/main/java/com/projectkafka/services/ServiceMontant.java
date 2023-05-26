package com.projectkafka.services;

import com.projectkafka.entities.Pli;
import org.springframework.stereotype.Service;

@Service
public class ServiceMontant {
    public double calculerMontant(Pli pli) {
        double poids = pli.getPoids();
        double largeur = pli.getLargeur();
        double longueur = pli.getLongueur();

        double montant = poids*largeur*longueur;

        return montant;
    }
}
