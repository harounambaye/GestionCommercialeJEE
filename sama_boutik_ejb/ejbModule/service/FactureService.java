package service;

import entity.*;
import dao.DetailCommandeDao;
import dao.LivraisonDao;
import dao.LivraisonDaoImpl;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class FactureService {

    @EJB
    private DetailCommandeDao detailCommandeDao;
    
    @EJB
    private LivraisonDao livraisonDao;


    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Génère une facture PDF pour une commande
     */
    public byte[] genererFacturePDF(Commande commande) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // En-tête
            ajouterEntete(document, commande);
            document.add(Chunk.NEWLINE);

            // Informations client
            ajouterInfosClient(document, commande);
            document.add(Chunk.NEWLINE);

            // Tableau des articles
            ajouterTableauArticles(document, commande);
            document.add(Chunk.NEWLINE);

            // Totaux
            ajouterTotaux(document, commande);
            document.add(Chunk.NEWLINE);

            // Pied de page
            ajouterPiedPage(document);

            document.close();
            System.out.println("✅ Facture générée pour: " + commande.getCodeCommande());
            
            return baos.toByteArray();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur génération facture: " + e.getMessage());
            throw e;
        }
    }

    private void ajouterEntete(Document doc, Commande commande) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        // Logo et nom entreprise (gauche)
        PdfPCell cellGauche = new PdfPCell();
        cellGauche.setBorder(Rectangle.NO_BORDER);
        Paragraph entreprise = new Paragraph("SAMA BOUTIK", FONT_TITLE);
        entreprise.add(new Paragraph("Entité Commerciale", FONT_NORMAL));
        cellGauche.addElement(entreprise);
        table.addCell(cellGauche);

        // Numéro facture (droite)
        PdfPCell cellDroite = new PdfPCell();
        cellDroite.setBorder(Rectangle.NO_BORDER);
        cellDroite.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph facture = new Paragraph("FACTURE", FONT_TITLE);
        facture.add(new Paragraph("N° " + commande.getCodeCommande(), FONT_BOLD));
        facture.add(new Paragraph("Date: " + commande.getDateCommande().format(DATE_FORMAT), FONT_NORMAL));
        cellDroite.addElement(facture);
        table.addCell(cellDroite);

        doc.add(table);
        doc.add(new Paragraph(" "));
        
        // Ligne de séparation
        LineSeparator line = new LineSeparator();
        doc.add(line);
    }

    private void ajouterInfosClient(Document doc, Commande commande) throws DocumentException {
        
    	Paragraph client = new Paragraph("CLIENT", FONT_BOLD);
        
        Livraison liv = livraisonDao.findByCommande(commande);

        client.add(new Paragraph("ID: " + commande.getIdPersonne(), FONT_NORMAL));
        
        if (liv != null) {
            if (liv.getAdresse() != null) {
                client.add(new Paragraph("Adresse: " + liv.getAdresse(), FONT_NORMAL));
            }
            if (liv.getTelephone() != null) {
                client.add(new Paragraph("Tél: " + liv.getTelephone(), FONT_NORMAL));
            }
        }
        
        doc.add(client);
    }

    private void ajouterTableauArticles(Document doc, Commande commande) throws DocumentException {
        List<DetailCommande> details = detailCommandeDao.findByCommande(commande);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4, 1, 2, 2});

        // En-têtes
        ajouterCelluleEntete(table, "Article");
        ajouterCelluleEntete(table, "Qté");
        ajouterCelluleEntete(table, "P.U.");
        ajouterCelluleEntete(table, "Total");

        // Lignes
        for (DetailCommande detail : details) {
            Article article = detail.getArticle();
            Produit produit = article.getProduit();
            
            table.addCell(new PdfPCell(new Phrase(produit.getNom(), FONT_NORMAL)));
            table.addCell(new PdfPCell(new Phrase("1", FONT_NORMAL)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.0f FCFA", article.getPrixUnitaire()), FONT_NORMAL)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.0f FCFA", article.getPrixUnitaire()), FONT_NORMAL)));
        }

        doc.add(table);
    }

    private void ajouterCelluleEntete(PdfPTable table, String texte) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, FONT_BOLD));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void ajouterTotaux(Document doc, Commande commande) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(new PdfPCell(new Phrase("Sous-total:", FONT_BOLD)));
        table.addCell(new PdfPCell(new Phrase(String.format("%.0f FCFA", commande.getTotal()), FONT_NORMAL)));

        if (commande.getTypecommande() != null && commande.getTypecommande().equals("AVEC_LIVRAISON")) {
            table.addCell(new PdfPCell(new Phrase("Frais de livraison:", FONT_BOLD)));
            table.addCell(new PdfPCell(new Phrase("1000 FCFA", FONT_NORMAL)));

            table.addCell(new PdfPCell(new Phrase("TOTAL:", FONT_BOLD)));
            
            BigDecimal totalAvecLivraison = commande.getTotal().add(BigDecimal.valueOf(1000));

            table.addCell(
            	    new PdfPCell(
            	        new Phrase(String.format("%.0f FCFA", totalAvecLivraison), FONT_BOLD)
            	    )
            	);        } else {
            table.addCell(new PdfPCell(new Phrase("TOTAL:", FONT_BOLD)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.0f FCFA", commande.getTotal()), FONT_BOLD)));
        }

        doc.add(table);
    }

    private void ajouterPiedPage(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        Paragraph pied = new Paragraph("Merci de votre confiance !", FONT_NORMAL);
        pied.setAlignment(Element.ALIGN_CENTER);
        doc.add(pied);
        
        Paragraph contact = new Paragraph("Contact: info@samaboutik.sn | Tél: +221 XX XXX XX XX", FONT_NORMAL);
        contact.setAlignment(Element.ALIGN_CENTER);
        doc.add(contact);
    }
}