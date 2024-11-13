package com.example.vetoapp.models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.example.vetoapp.models.Animal;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFExportUtility {

    public static File createAnimalPDF(Context context, List<Animal> animals) {
        Document document = new Document();
        File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Animal_Info.pdf");

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            // Title
            document.add(new Paragraph("Your Pets Informations\n\n"));

            for (Animal animal : animals) {
                 Paragraph animalInfo = new Paragraph();

                animalInfo.add("Name: " + animal.getName() + "\n");
                animalInfo.add("Type: " + animal.getType() + "\n");
                animalInfo.add("Age: " + animal.getAge() + "\n");
                animalInfo.add("Sex: " + animal.getSex() + "\n");
                animalInfo.add("Weight: " + animal.getWeight() + "\n");
                animalInfo.add("Behavior: " + animal.getBehavior() + "\n");
                animalInfo.add("\n-------------------------------\n\n");

                // Add the paragraph to the document
                document.add(animalInfo);
            }

        } catch (DocumentException | IOException e) {
            Log.e("PDFExportUtility", "Error creating PDF", e);
        } finally {
            document.close();
        }

        return pdfFile;
    }

}
