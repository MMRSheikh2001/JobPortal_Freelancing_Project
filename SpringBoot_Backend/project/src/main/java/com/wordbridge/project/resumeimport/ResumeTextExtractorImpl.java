package com.wordbridge.project.resumeimport;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ResumeTextExtractorImpl implements ResumeTextExtractor {


    @Override
    public String extractText(Path path) {


        if (path.toString().toLowerCase().endsWith(".pdf")) {
            return extractTextFromPdf(path);
        }

        if (path.toString().toLowerCase().endsWith(".docx")) {
            return extractTextFromPdf(path);
        }


        throw new RuntimeException(
                "Only PDF and DOCX files are supported."
        );

    }


    private String extractTextFromPdf(Path path) {

        try (PDDocument document = Loader.loadPDF(path.toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String extractTextFromDocx(Path path) throws IOException {

        try (XWPFDocument document =
                     new XWPFDocument(Files.newInputStream(path))) {

            StringBuilder builder =
                    new StringBuilder();

            for (XWPFParagraph paragraph : document.getParagraphs()) {

                builder.append(paragraph.getText());

                builder.append("\n");

            }

            try {
                document.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return builder.toString();

        }


    }


}
