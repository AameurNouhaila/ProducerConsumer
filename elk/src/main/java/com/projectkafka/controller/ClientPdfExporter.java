package com.projectkafka.controller;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import com.projectkafka.entities.Client;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ClientPdfExporter {
    private List<Client> listClients;

    public byte[] export() throws IOException, FileNotFoundException, MalformedURLException {
        String path = "facture.pdf";

        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        // Header
        float twocol = 285f;
        float twocol150 = twocol + 150f;
        float twocolumnWidth[] = {twocol150, twocol};

        String imagePath = "C:\\Users\\naameur\\Desktop\\kafka-la-poste\\elk\\target\\poste.png";
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image1 = new Image(imageData);

        // Set the desired width and height
        float desiredWidth = 80; // Specify the width in points
        float desiredHeight = 80; // Specify the height in points
        image1.setWidth(desiredWidth);
        image1.setHeight(desiredHeight);

        Table table = new Table(2);
        table.addCell(new Cell().add(image1));

        Table nestedTable = new Table(new float[]{twocol / 2, twocol / 2});

        nestedTable.addCell(new Cell().add("FACTURE N° :").setBorder(Border.NO_BORDER))
                .setTextAlignment(TextAlignment.RIGHT);
        nestedTable.addCell(new Cell().add("962922952").setBorder(Border.NO_BORDER))
                .setTextAlignment(TextAlignment.RIGHT);
        nestedTable.addCell(new Cell().add("Date :").setBorder(Border.NO_BORDER))
                .setTextAlignment(TextAlignment.RIGHT);
        nestedTable.addCell(new Cell().add("12-05-2023").setBorder(Border.NO_BORDER))
                .setTextAlignment(TextAlignment.RIGHT);

        table.addCell(new Cell().add(nestedTable));

        document.add(table);

        Paragraph invoiceText = new Paragraph("FACTURE")
                .setFontSize(30f)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20f);

        document.add(invoiceText);

        Table tableHeader = new Table(twocolumnWidth);
        tableHeader.addCell(new Cell().add("").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());

        Border upperBorder = new SolidBorder(Color.GRAY, 1f / 2f);
        Table divider = new Table(new float[]{twocol * 3});
        divider.setBorder(upperBorder);

        document.add(tableHeader);

        document.add(divider);


        // Client Information
        float colWidth[] = {120, 250, 100, 80};
        Table clientInfoTable = new Table(colWidth);



        clientInfoTable.addCell(new Cell().add("Adresse :").setBorder(Border.NO_BORDER));
        clientInfoTable.addCell(new Cell().add("LAPOSTE-9 rue u Colonel Pierre Avia ").setBorder(Border.NO_BORDER));
        clientInfoTable.addCell(new Cell().add("Ville :").setBorder(Border.NO_BORDER));
        clientInfoTable.addCell(new Cell().add("Paris , France").setBorder(Border.NO_BORDER));

        clientInfoTable.addCell(new Cell().add("Code postal :").setBorder(Border.NO_BORDER));
        clientInfoTable.addCell(new Cell().add("17507").setBorder(Border.NO_BORDER));
        clientInfoTable.addCell(new Cell().add("Téléphone :").setBorder(Border.NO_BORDER));
        clientInfoTable.addCell(new Cell().add("09 96 399 111").setBorder(Border.NO_BORDER));

        document.add(clientInfoTable);

        float ItemInfoColWidth[] = {140, 140, 140, 140};
        Table itemInfoTable = new Table(ItemInfoColWidth);



        document.add(new Paragraph("\n"));

        itemInfoTable.addCell(new Cell().add("Nom")
                .setBackgroundColor(new DeviceRgb(0, 112, 173))
                .setFontColor(Color.WHITE)
                .setBold());

        itemInfoTable.addCell(new Cell().add("Prenom")
                .setBackgroundColor(new DeviceRgb(0, 112, 173))
                .setFontColor(Color.WHITE)
                .setBold());

        itemInfoTable.addCell(new Cell().add("Date Evenement")
                .setBackgroundColor(new DeviceRgb(0, 112, 173))
                .setFontColor(Color.WHITE)
                .setBold());

        itemInfoTable.addCell(new Cell().add("Montant")
                .setBackgroundColor(new DeviceRgb(0, 112, 173))
                .setFontColor(Color.WHITE)
                .setBold());

        double totalAmount = 0.0; // Variable to hold the total amount

        for (Client client : listClients) {
            itemInfoTable.addCell(client.getNom());
            itemInfoTable.addCell(client.getPrenom());
            itemInfoTable.addCell(String.valueOf(client.getDateEventFormatted()));
            itemInfoTable.addCell(String.valueOf(client.getMontant() + " €"));

            // Calculate the total amount
            totalAmount += client.getMontant();
        }

        itemInfoTable.addCell(new Cell(1, 3).add("Montant total").setFontColor(Color.BLACK)
                .setBackgroundColor(Color.WHITE)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT));
        itemInfoTable.addCell(new Cell().add(String.valueOf(totalAmount + " €"))
                .setBackgroundColor(new DeviceRgb(0, 112, 173))
                .setFontColor(Color.WHITE)
                .setBold());

        document.add(itemInfoTable);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        Table tb = new Table(new float[]{twocol * 3});
        tb.addCell(new Cell().add("Conditions de paiment:").setBold().setBorder(Border.NO_BORDER));
        List<String> TncList = new ArrayList<>();

        TncList.add("1. Les factures doivent être réglées dans un délai de 30 jours à compter de la date d'émission de la facture, sauf accord préalable avec La Poste.");
        TncList.add(
                "2. Tout paiement effectué après la date d'échéance indiquée sur la facture sera considéré comme tardif. " );

        TncList.add( "3. En cas de retard de paiement, des pénalités de retard seront appliquées conformément à la réglementation en vigueur.");


        for (String tnc : TncList) {
            tb.addCell(new Cell().add(tnc).setBorder(Border.NO_BORDER));
        }
        document.add(tb);
        document.close();

        return outputStream.toByteArray();
    }

    public ResponseEntity<byte[]> downloadDocument() throws Exception {
        byte[] pdfBytes = export();
        System.out.println("The document is downloaded successfully!");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"facture.pdf\"")
                .body(pdfBytes);
    }
}
