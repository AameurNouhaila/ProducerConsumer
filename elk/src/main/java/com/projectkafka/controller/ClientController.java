package com.projectkafka.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.projectkafka.entities.Client;
import com.projectkafka.services.ClientService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/la-poste")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public ResponseEntity<Page<Client>> getAllClients(
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "2") int size,
            @RequestParam(name="search", defaultValue = "") String search
            ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Client> clients = clientService.chercher(search, pageRequest);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/clients/date")
    public ResponseEntity<Page<Client>> getClientsByDate(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "2") int size,
            @RequestParam(name = "dateDebut") @DateTimeFormat(pattern = "yyyy-MM-dd") String dateDebut,
            @RequestParam(name = "dateFin") @DateTimeFormat(pattern = "yyyy-MM-dd") String dateFin
    ) {
        LocalDateTime parsedDateDebut = LocalDateTime.parse(dateDebut + "T00:00:00");
        LocalDateTime parsedDateFin = LocalDateTime.parse(dateFin + "T23:59:59");

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Client> clients = clientService.chercherParDate(parsedDateDebut, parsedDateFin, pageRequest);
        return ResponseEntity.ok(clients);
    }


    @GetMapping("/clients/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-disposition";
        String headerValue = "attachment; filename=facture.pdf";

        response.setHeader(headerKey, headerValue);

        Page<Client> clientsPage = clientService.listAll();
        List<Client> listClients = clientsPage.getContent(); // Obtenir la liste de clients à partir de la page

        ClientPdfExporter exporter = new ClientPdfExporter(listClients);
        exporter.export();

    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadDocument(HttpServletResponse response) throws Exception {
        // Appeler la méthode exportToPdf pour générer le document PDF
        exportToPdf(response);

        // Récupérer le fichier PDF généré
        File pdfFile = new File("facture.pdf");

        // Lire le contenu du fichier PDF
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        byte[] documentContent = new byte[(int) pdfFile.length()];
        fileInputStream.read(documentContent);
        fileInputStream.close();

        // Définir les en-têtes de la réponse
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("facture.pdf").build());

        // Retourner la réponse avec le contenu du fichier PDF
        return ResponseEntity.ok().headers(headers).body(documentContent);
    }



}
