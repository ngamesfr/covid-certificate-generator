package controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.DatatypeConverter;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFGenerator {
  private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private PDDocument document;
  private PDPage page;
  private float currentOffset;

  public PDFGenerator() {
    this.document = new PDDocument();
    this.page = new PDPage();
    this.document.addPage(this.page);
    this.currentOffset = 0;
  }

  public byte[] generate(final User user) throws IOException {
    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
      drawTitle(contentStream);
      drawSubTitle(contentStream);
      drawIdentity(contentStream, user);
      drawCertificateLine(contentStream);
      drawWorkOption(contentStream, user);
      drawGroceryOption(contentStream, user);
      drawHealthOption(contentStream, user);
      drawFamilyOption(contentStream, user);
      drawExerciseOption(contentStream, user);
      drawSignature(contentStream, user);
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    document.save(byteArrayOutputStream);
    document.close();

    return byteArrayOutputStream.toByteArray();
  }

  private void drawTitle(final PDPageContentStream contentStream) throws IOException {
    this.currentOffset += 60;
    centerText(contentStream, 14, true, "ATTESTATION DE DÉPLACEMENT DÉROGATOIRE");
  }

  private void drawSubTitle(final PDPageContentStream contentStream) throws IOException {
    this.currentOffset += 30;
    centerText(contentStream, 11, false,
               "En application de l’article 1er du décret du 16 mars 2020 portant réglementation des");
    this.currentOffset += 15;
    centerText(contentStream, 11, false,
               "déplacements dans le cadre de la lutte contre la propagation du virus Covid-19 :");
  }

  private void drawCertificateLine(final PDPageContentStream contentStream) throws IOException {
    this.currentOffset += 50;
    text(contentStream, 45, 12.5f, false,
         "certifie que mon déplacement est lié au motif suivant (cocher la case) autorisé par");
    this.currentOffset += 15;
    text(contentStream, 45, 12.5f, false,
         "l’article 1er du décret du 16 mars 2020 portant réglementation des déplacements dans");
    this.currentOffset += 15;
    text(contentStream, 45, 12.5f, false, "le cadre de la lutte contre la propagation du virus Covid-19 :");
  }

  private void drawIdentity(final PDPageContentStream contentStream, final User user) throws IOException {
    this.currentOffset += 50;
    text(contentStream, 45, 12.5f, false, "Je soussigné(e)");
    this.currentOffset += 30;
    text(contentStream, 45, 12.5f,false, "Mme / M.");
    text(contentStream, 150, 12.5f, false, user.firstName + " " + user.lastName);
    this.currentOffset += 30;
    text(contentStream, 45, 12.5f,false, "Né(e) le :");
    text(contentStream, 150, 12.5f, false, user.birthDate.format(DATE_PATTERN));
    this.currentOffset += 30;
    text(contentStream, 45, 12.5f, false, "Demeurant :");
    text(contentStream, 150, 12.5f, false, user.address);
    if (user.address2 != null && !user.address2.isEmpty()) {
      this.currentOffset += 20;
      text(contentStream, 150, 12.5f, false, user.address2);
    }
    this.currentOffset += 20;
    text(contentStream, 150, 12.5f, false, user.zip + " " + user.city);
  }

  private void drawWorkOption(final PDPageContentStream contentStream, final User user) throws IOException {
    this.currentOffset += 50;
    drawCheckbox(contentStream, user.reason.equals(Reason.WORK));
    text(contentStream, 70, 12.5f, false,"déplacements entre le domicile et le lieu d’exercice de l’activité professionnelle,");
    this.currentOffset += 15;
    text(contentStream, 70, 12.5f, false,"lorsqu’ils sont indispensables à l’exercice d’activités ne pouvant être organisées");
    this.currentOffset += 15;
    text(contentStream, 70, 12.5f, false,"sous forme de télétravail (sur justificatif permanent) ou déplacements");
    this.currentOffset += 15;
    text(contentStream, 70, 12.5f, false,"professionnels ne pouvant être différés;");
  }

  private void drawGroceryOption(final PDPageContentStream contentStream, final User user) throws IOException {
    this.currentOffset += 30;
    drawCheckbox(contentStream, user.reason.equals(Reason.GROCERY));
    text(contentStream, 70, 12.5f, false,"déplacements pour effectuer des achats de première nécessité dans des");
    this.currentOffset += 15;
    text(contentStream, 70, 12.5f, false,"établissements autorisés (liste sur gouvernement.fr);");
  }

  private void drawHealthOption(final PDPageContentStream contentStream, final User user) throws IOException {
    this.currentOffset += 30;
    drawCheckbox(contentStream, user.reason.equals(Reason.HEALTH));
    text(contentStream, 70, 12.5f, false,"déplacements pour motif de santé;");
  }

  private void drawFamilyOption(final PDPageContentStream contentStream, final User user) throws IOException {
    this.currentOffset += 30;
    drawCheckbox(contentStream, user.reason.equals(Reason.FAMILY));
    text(contentStream, 70, 12.5f, false,"déplacements pour motif familial impérieux, pour l’assistance aux personnes");
    this.currentOffset += 15;
    text(contentStream, 70, 12.5f, false,"vulnérables ou la garde d’enfants;");
  }

  private void drawExerciseOption(final PDPageContentStream contentStream, final User user) throws IOException {
    this.currentOffset += 30;
    drawCheckbox(contentStream, user.reason.equals(Reason.EXERCISE));
    text(contentStream, 70, 12.5f, false,"déplacements brefs, à proximité du domicile, liés à l’activité physique individuelle");
    this.currentOffset += 15;
    text(contentStream, 70, 12.5f, false,"des personnes, à l’exclusion de toute pratique sportive collective, et aux besoins");
    this.currentOffset += 15;
    text(contentStream, 70, 12.5f, false,"des animaux de compagnie.");
  }

  private void drawCheckbox(final PDPageContentStream contentStream, final boolean checked) throws IOException {
    PDImageXObject image = PDImageXObject.createFromFileByContent(new File("pdf-resources/" + (checked?"checked":"unchecked") + ".png"), document);
    contentStream.drawImage(image, 45f, page.getMediaBox().getHeight() - this.currentOffset - image.getHeight() / 2.2f, image.getWidth() / 3f, image.getHeight() / 3f);
  }

  private void drawSignature(final PDPageContentStream contentStream, final User user) throws IOException {
    this.currentOffset += 40;
    rightText(contentStream, 45,12, false, "Fait à " + user.city + ", le " + LocalDate.now().format(DATE_PATTERN));
    this.currentOffset += 20;

    byte[] imageData = DatatypeConverter.parseBase64Binary(user.signature.substring(user.signature.indexOf(',') + 1));
    PDImageXObject image = PDImageXObject.createFromByteArray(document, imageData, "signature.png");
    contentStream.drawImage(image, page.getMediaBox().getWidth() - 75f - image.getWidth() / 3f, page.getMediaBox().getHeight() - this.currentOffset - image.getHeight() / 2.5f, image.getWidth() / 2.5f, image.getHeight() / 2.5f);
  }

  private void centerText(PDPageContentStream contentStream, int fontSize, final boolean bold,
                          String text)
      throws IOException {
    final PDFont font = getFont(bold);
    final float textWidth = font.getStringWidth(text) / 1000 * fontSize;
    final float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

    contentStream.beginText();
    contentStream.setFont(font, fontSize);
    contentStream.newLineAtOffset((page.getMediaBox().getWidth() - textWidth) / 2, page.getMediaBox().getHeight() - textHeight - this.currentOffset);
    contentStream.showText(text);
    contentStream.endText();
  }

  private void text(PDPageContentStream contentStream, int offsetLeft, float fontSize,
                    final boolean bold, String text)
      throws IOException {
    final PDFont font = getFont(bold);
    final float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

    contentStream.beginText();
    contentStream.setFont(font, fontSize);
    contentStream.newLineAtOffset(offsetLeft, page.getMediaBox().getHeight() - textHeight - this.currentOffset);
    contentStream.showText(text);
    contentStream.endText();
  }

  private void rightText(PDPageContentStream contentStream, int rightOffset, int fontSize, final boolean bold,
                          String text)
      throws IOException {
    final PDFont font = getFont(bold);
    final float textWidth = font.getStringWidth(text) / 1000 * fontSize;
    final float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

    contentStream.beginText();
    contentStream.setFont(font, fontSize);
    contentStream.newLineAtOffset(page.getMediaBox().getWidth() - textWidth - rightOffset, page.getMediaBox().getHeight() - textHeight - this.currentOffset);
    contentStream.showText(text);
    contentStream.endText();
  }

  private PDFont getFont(final boolean bold) throws IOException {
    final Encoding encoding = Encoding.getInstance(COSName.WIN_ANSI_ENCODING);
    final String fontFileName = "pdf-resources/Roboto-" + (bold ? "Bold": "Regular") + ".ttf";
    return PDTrueTypeFont.load(document, new File(fontFileName), encoding);
  }
}
