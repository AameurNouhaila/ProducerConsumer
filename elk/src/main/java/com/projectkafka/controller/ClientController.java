package com.projectkafka.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.projectkafka.entities.Client;
import com.projectkafka.services.ClientService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.filters.ExpiresFilter;
import org.elasticsearch.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
@RestController
@RequestMapping("/la-poste")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients/list")
    public ResponseEntity<List<Client>> getAllClientsList() {
        List<Client> clientsList = clientService.listAll().getContent();
        return ResponseEntity.ok(clientsList);
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

/*
   @GetMapping("/clients/pdf")
    public void generatePdf(HttpServletResponse response,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "2") int size,
                            @RequestParam(name = "dateDebut") String dateDebut,
                            @RequestParam(name = "dateFin") String dateFin) throws IOException {

        // Set response headers
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=facture.pdf";
        response.setHeader(headerKey, headerValue);

        // Generate the PDF content using the provided dates
        byte[] pdfBytes = exportToPdf(page, size, dateDebut, dateFin);

        // Write the PDF content to the response's output stream
        response.getOutputStream().write(pdfBytes);
    }



    @GetMapping("/clients/download")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "2") int size,
            @RequestParam(name = "dateDebut") String dateDebut,
            @RequestParam(name = "dateFin") String dateFin) throws Exception {

        // Generate the PDF content using the provided dates
        byte[] pdfBytes = exportToPdf(page, size, dateDebut, dateFin);

        // Set response headers for download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "facture.pdf");

        // Return the response with the PDF content
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    private byte[] exportToPdf(int page, int size, String dateDebut, String dateFin) throws IOException {
        // Parse the date strings into LocalDateTime objects using ISO 8601 format
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime parsedDateDebut = LocalDateTime.parse(dateDebut, isoFormatter);
        LocalDateTime parsedDateFin = LocalDateTime.parse(dateFin, isoFormatter);

        // Convert LocalDateTime to LocalDate and add time for start and end of the day
        LocalDate startDate = parsedDateDebut.toLocalDate();
        LocalDate endDate = parsedDateFin.toLocalDate();

        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);


        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Client> clients = clientService.chercherParDate(startOfDay, endOfDay, pageRequest);
        List<Client> listClients = clients.getContent();

        ClientPdfExporter exporter = new ClientPdfExporter(listClients);

        // Export PDF and return the byte array
        return exporter.export();
    }*/




    @GetMapping("/clients/pdf")
    public void exportToPdf(HttpServletResponse response,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "10") int size,
                            @RequestParam(name = "dateDebut") String dateDebut,
                            @RequestParam(name = "dateFin") String dateFin) throws IOException {

        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-disposition";
        String headerValue = "attachment; filename=facture.pdf";



        // Parse the date strings into LocalDateTime objects using ISO 8601 format
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime parsedDateDebut = LocalDateTime.parse(dateDebut, isoFormatter);
        LocalDateTime parsedDateFin = LocalDateTime.parse(dateFin, isoFormatter);

        // Convert LocalDateTime to LocalDate and add time for start and end of the day
        LocalDate startDate = parsedDateDebut.toLocalDate();
        LocalDate endDate = parsedDateFin.toLocalDate();

        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Client> clients = clientService.chercherParDate(startOfDay, endOfDay, pageRequest);
        List<Client> listClients = clients.getContent();

        ClientPdfExporter exporter = new ClientPdfExporter(listClients);
        exporter.export();
    }


    @GetMapping("/clients/download")
    public ResponseEntity<byte[]> downloadDocument(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "dateDebut") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") String dateDebut,
            @RequestParam(name = "dateFin") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") String dateFin,
            HttpServletResponse response) throws Exception {

        // Generate the PDF file using the provided dates
        exportToPdf(response, page, size, dateDebut, dateFin);

        // Read the generated PDF file
        File pdfFile = new File("facture.pdf");
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        byte[] documentContent = new byte[(int) pdfFile.length()];
        fileInputStream.read(documentContent);
        fileInputStream.close();

        // Define the headers of the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("facture.pdf").build());

        // Return the response with the content of the PDF file
        return ResponseEntity.ok().headers(headers).body(documentContent);
    }


    @DeleteMapping("/clients/{_id}")
    public ResponseEntity<String> supprimerClientParId(@PathVariable String _id) {
        // Utilisation de la méthode supprimerClientParId du service
        clientService.supprimerClientParId(_id);

        return new ResponseEntity<>("Le client a été supprimé avec succès.", HttpStatus.OK);
    }

    @GetMapping("/clients/findById/{_id}")
    public ResponseEntity<Client> getClientById(@PathVariable String _id) {
        // Utilisation de la méthode getClientById du service
        Client client = clientService.getClientById(_id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
