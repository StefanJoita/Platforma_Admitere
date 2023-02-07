package aplicatie_admitere.servicies;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

import aplicatie_admitere.models.IerarhizareDetails;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import com.itextpdf.text.pdf.PdfWriter;

import javax.naming.MalformedLinkException;

@Service

public class PdfGeneratorService {

    @SneakyThrows
    public byte[] genereazaLegitimatie(String name, String cnp, String phone,Long id) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try{
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.setPageSize(new Rectangle(600, 320));
            document.open();
            URL url=getClass().getResource("/stema.png");
            Image image = Image.getInstance(url);
            image.scaleToFit(100, 100);
            image.setAbsolutePosition(10, 10);
            document.add(image);

            Paragraph title = new Paragraph("ADMITERE ACADEMIA TEHNICA MILITARA");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(""));
            Font font = new Font(Font.FontFamily.HELVETICA, 12);
            Paragraph subtitle = new Paragraph("LEGITIMATIE DE CANDIDAT", font);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            Paragraph p = new Paragraph("NR. Candidat: ");
            p.add(new Chunk("" + id, boldFont));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            Paragraph p1 = new Paragraph("Nume: ");
            p1.add(new Chunk("" + name, boldFont));
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);

            Paragraph p2 = new Paragraph("CNP: ");
            p2.add(new Chunk("" + cnp, boldFont));
            p2.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);

            Paragraph p3 = new Paragraph("Telefon: ");
            p3.add(new Chunk("" + phone, boldFont));
            p3.setAlignment(Element.ALIGN_CENTER);
            document.add(p3);
            document.close();
        }
        catch(DocumentException e)
        {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    public void genereazaArhivaSesiune(List<List<String>> listaCandidati, String fileName)
            throws FileNotFoundException, DocumentException {
        File folder = new File("arhivare sesiune");
        if (!folder.exists()) {
            folder.mkdir();
        }
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(folder.getAbsolutePath() + File.separator + fileName));
        document.open();

        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font boldFont2 = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Paragraph title = new Paragraph("DATELE SESIUNII",boldFont2);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        for (List<String> list : listaCandidati) {
            {
                Paragraph p0 = new Paragraph("Nume candidat:  ",boldFont);
                p0.add(new Chunk("" + list.get(0)));
                p0.setAlignment(Element.ALIGN_LEFT);
                document.add(p0);

                Paragraph p1 = new Paragraph("Email: ",boldFont);
                p1.add(new Chunk("" + list.get(1)));
                p1.setAlignment(Element.ALIGN_LEFT);
                document.add(p1);

                Paragraph p2 = new Paragraph("CNP: ",boldFont);
                p2.add(new Chunk("" + list.get(2)));
                p2.setAlignment(Element.ALIGN_LEFT);
                document.add(p2);

                Paragraph p3 = new Paragraph("Telefon: ",boldFont);
                p3.add(new Chunk("" + list.get(3)));
                p3.setAlignment(Element.ALIGN_LEFT);
                document.add(p3);

                Paragraph p4 = new Paragraph("Optiuni specializari: ",boldFont);
                p4.add(new Chunk("\n" + list.get(4) + "\n" + list.get(5) + "\n" + list.get(6)));
                p4.setAlignment(Element.ALIGN_LEFT);
                document.add(p4);

                Paragraph p5 = new Paragraph("Rezultatul ierarhizarii: ",boldFont);
                p5.add(new Chunk("" + list.get(7)));
                p5.setAlignment(Element.ALIGN_LEFT);
                document.add(p5);
                document.add(new Paragraph("\n "));
            }
        }
        document.close();


    }

    @SneakyThrows
    public byte[] generareIerarhizare(List<IerarhizareDetails> responseList) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.setPageSize(new Rectangle(3508,  2480));
            document.open();
          /*  URL url = getClass().getResource("/stema.png");
            Image image = Image.getInstance(url);
            image.scaleToFit(100, 100);
            image.setAbsolutePosition(10, 10);
            document.add(image);*/


            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 72, Font.BOLD);
            Paragraph title = new Paragraph("IERARHIZARE SESIUNE ADMITERE 2022-2023",titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(""));
            Paragraph subtitle = new Paragraph("ACADEMIA TEHNICA MILITARA FERDINAND I", titleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 48, Font.BOLD);
            Font foont = new Font(Font.FontFamily.TIMES_ROMAN, 40);

            PdfPTable table = new PdfPTable(4); // 3 columns
            table.setWidthPercentage(100); // width of the table
            table.setSpacingBefore(10f); // space before the table
            table.setSpacingAfter(10f); // space after the table

// Set the column widths
            float[] columnWidths = {1f, 1f, 1f,5f};
            table.setWidths(columnWidths);

// Create the first row
            PdfPCell cell1 = new PdfPCell(new Paragraph("ID Candidat",boldFont));
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Nota",boldFont));
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Paragraph("REZULTAT",boldFont));
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Paragraph("SPECIALIZARE",boldFont));
            cell4.setPaddingLeft(10);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell4);


            for(IerarhizareDetails ierarhizareDetails : responseList)
            {

                PdfPCell cell11 = new PdfPCell();
                Paragraph p1 = new Paragraph(" ");
                p1.add(new Chunk(""+ierarhizareDetails.getId_Candidat(), foont));
                cell11.addElement(p1);
                cell11.setPaddingLeft(10);
                cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell11);

                PdfPCell cell22 = new PdfPCell();
                Paragraph p2 = new Paragraph("");
                p2.add(new Chunk(""+ierarhizareDetails.getNota(), foont));
                cell22.addElement(p2);
                cell22.setPaddingLeft(10);
                cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell22.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell22);

                PdfPCell cell33 = new PdfPCell();
                Paragraph p3 = new Paragraph(" ");
                p3.add(new Chunk(""+ierarhizareDetails.getStatus(),foont));
                cell33.addElement(p3);
                cell33.setPaddingLeft(10);
                cell33.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell33.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell33);

                PdfPCell cell44 = new PdfPCell();
                Paragraph p4 = new Paragraph();
                    p4.add("\n\n");
                    String specializari = String.join("\n\n\n", ierarhizareDetails.getSpecializare());
                    p4.add(new Chunk(specializari, foont));
                    p4.add("\n\n");
                cell44.addElement(p4);
                cell44.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell44.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell44);

                /*
            Paragraph p = new Paragraph("Nume: ");
            p.add(new Chunk("" + ierarhizareDetails.getNume(), boldFont));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            Paragraph p1 = new Paragraph("CNP: ");
            p1.add(new Chunk("" + ierarhizareDetails.getCnp(), boldFont));
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);

            Paragraph p2 = new Paragraph("Medie obținuta: ");
            p2.add(new Chunk("" + ierarhizareDetails.getMedie(), boldFont));
            p2.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);


            Paragraph p3 = new Paragraph("Specializari alese: ");
                String specializari = String.join(",", ierarhizareDetails.getSpecializari());
                p3.add(new Chunk(specializari, boldFont));
            p3.setAlignment(Element.ALIGN_CENTER);
            document.add(p3);
                */
            }
            document.add(table);
            document.close();
        }
        catch(DocumentException e)
        {
            e.printStackTrace();
        }
        return out.toByteArray();

    }
}


