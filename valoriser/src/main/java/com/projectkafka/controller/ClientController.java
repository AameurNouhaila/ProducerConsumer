package com.projectkafka.controller;

import com.projectkafka.entities.Client;
import com.projectkafka.entities.Evenement;
import com.projectkafka.entities.NiveauRecommandation;
import com.projectkafka.entities.Pli;
import com.projectkafka.repositorie.ClientRepository;
import com.projectkafka.services.ServiceMontant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ServiceMontant serviceMontant;

    public ClientController(ClientRepository clientRepository, ServiceMontant serviceMontant) {
        this.clientRepository = clientRepository;
        this.serviceMontant = serviceMontant;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> modifierMontantPli(@PathVariable Integer id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }



        Client client = clientOptional.get();
        List<Evenement> evenements = client.getEvenements();
        boolean modificationSucceeded = false;
        for (Evenement evenement : evenements) {
            List<Pli> plis = evenement.getPlis();
            for (Pli pli : plis) {
                double poids = pli.getPoids();
                NiveauRecommandation niveauRecommandation = pli.getNiveauRecommandation();
                double montant;

                if (poids >= 1 && poids <= 250 && niveauRecommandation == NiveauRecommandation.S1) {
                    pli.setMontant(4);
                } else if (poids >= 250 && poids <= 500 && niveauRecommandation == NiveauRecommandation.S2) {
                    pli.setMontant(8);
                } else if (poids > 500 && niveauRecommandation == NiveauRecommandation.S3){
                    pli.setMontant(20);
                } else {
                    System.out.println("Aucun pli correspondant aux critères spécifiés n'a été trouvé.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

                }

                modificationSucceeded = true;
            }
        }

        if (modificationSucceeded) {
            Client modifiedClient = clientRepository.save(client);
            return ResponseEntity.ok(modifiedClient);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
