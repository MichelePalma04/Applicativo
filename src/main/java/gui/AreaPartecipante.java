package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class AreaPartecipante {
    private JLabel avviso;
    private JButton homeButton;
    private JComboBox <Team> comboBox1;
    private JButton uniscitiButton;
    private JLabel benvenuto;
    private String loginPartecipante;
    private int eventoId;
    private JLabel teamLabel;
    private JPanel panel;
    private JLabel messaggio;
    private JButton sfogliaDocumenti;
    private JLabel inserisciDocumento;
    private JPanel panel2;
    private JLabel problema;
    private JButton creaTeamButton;
    private JTextField nomeField;
    private JLabel nomeTeam;
    private Controller controller;
    private JFrame frameAreaPartecipante;
    private JFrame frameEventi;
    private JFrame frameAccedi;
    private JFrame frameNotifica;
    private JFrame frameGiudice;

    private static final String FONT_FAMILY = "SansSerif";
    private static final String PROBLEMA_LABEL = "Problema da risolvere: ";
    private static final String DOCUMENTO_CARICATO_LABEL = "Documento caricato: ";
    private static final String NESSUN_DOCUMENTO_LABEL = "Nessun documento caricato dal tuo team.";
    private static final String COMMENTO_GIUDICE_LABEL = "Nessun commento dai giudici.";

    private Color bgColor = new Color(240, 248, 255);
    private Color btnColor = new Color(30, 144, 255);
    private Color btnHoverColor = new Color(65, 105, 225);
    private Color fieldBg = Color.WHITE;
    private Color fieldBorder = new Color(210, 210, 210);
    private Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
    private Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);


    public AreaPartecipante(String partecipanteLogin, int idEvento, JFrame frameAreaEventi, JFrame frameAreaAccesso, JFrame frameAreaNotifiche, JFrame frameAreaGiudice, Controller controller) {
        this.controller = controller;
        this.loginPartecipante = partecipanteLogin;
        this.eventoId = idEvento;
        frameEventi = frameAreaEventi;
        frameAccedi = frameAreaAccesso;
        frameNotifica = frameAreaNotifiche;
        frameGiudice = frameAreaGiudice;

        setupStileComponenti();
        setupFrame();

        List<Team> teams = controller.getTeamsEvento(eventoId);
        caricaTeamComboBox(teams);

        Team teamUtente = trovaTeamUtente(teams);
        boolean inTeam = teamUtente != null;

        JPanel documentiPanel = creaDocumentiPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(documentiPanel);

        aggiornaVisibilitaCampi(inTeam, teamUtente);

        if (inTeam) {
            mostraDocumentiTeam(documentiPanel, teamUtente);
        }

        creaTeamButton.addActionListener(e -> azioneCreaTeam(documentiPanel));
        homeButton.addActionListener(e -> azioneHome());
        uniscitiButton.addActionListener(e -> azioneUniscitiTeam(documentiPanel));
        sfogliaDocumenti.addActionListener(e -> azioneCaricaDocumento(documentiPanel));
    }

    private void setupStileComponenti() {
        setPanelBg(panel, bgColor);
        setPanelBg(panel2, bgColor);

        setLabelFont(benvenuto, labelFont);
        setLabelFont(teamLabel, labelFont);
        setLabelFont(avviso, labelFont);
        setLabelFont(messaggio, fieldFont);
        setLabelFont(problema, labelFont);
        setLabelFont(nomeTeam, labelFont);
        setLabelFont(inserisciDocumento, fieldFont);

        JButton[] allButtons = {homeButton, uniscitiButton, sfogliaDocumenti, creaTeamButton};
        for (JButton btn : allButtons) {
            if (btn != null) styleButton(btn);
        }

        styleField(comboBox1, fieldFont, fieldBg, fieldBorder);
        styleField(nomeField, fieldFont, fieldBg, fieldBorder);
    }

    private void setPanelBg(JPanel p, Color color) {
        if (p != null) p.setBackground(color);
    }

    private void setLabelFont(JLabel l, Font font) {
        if (l != null) l.setFont(font);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(btnColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(labelFont);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(btnHoverColor);}
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(btnColor);}
        });
    }

    private void styleField(JComponent f, Font font, Color bg, Color borderColor) {
        if (f != null) {
            f.setFont(font);
            f.setBackground(bg);
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }
    }

    private void setupFrame() {
        frameAreaPartecipante = new JFrame("Area Personale " + loginPartecipante);
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(600, 600);
        frameAreaPartecipante.setLocationRelativeTo(null);
        frameAreaPartecipante.setDefaultCloseOperation(EXIT_ON_CLOSE);

        messaggio.setVisible(false);
        avviso.setText("Iscrizione avvenuta con successo!");
        benvenuto.setText("Benvenuto, " + loginPartecipante);
    }

    private void caricaTeamComboBox(List<Team> teams) {
        if (teams.isEmpty()) {
            uniscitiButton.setEnabled(false);
            comboBox1.setEnabled(false);
            teamLabel.setText("Nessun team disponibile a cui unirsi.");
        } else {
            for (Team team : teams) {
                comboBox1.addItem(team);
            }
        }
    }

    private Team trovaTeamUtente(List<Team> teams) {
        for (Team team : teams) {
            if (controller.isPartecipanteInTeam(loginPartecipante, team.getNomeTeam(), eventoId)) {
                return team;
            }
        }
        return null;
    }

    private JPanel creaDocumentiPanel() {
        JPanel documentiPanel = new JPanel();
        documentiPanel.setBackground(bgColor);
        documentiPanel.setLayout(new BoxLayout(documentiPanel, BoxLayout.Y_AXIS));
        return documentiPanel;
    }

    private void aggiornaVisibilitaCampi(boolean inTeam, Team teamUtente) {
        if (inTeam) {
            setVisibilitaCampiTeam(true, teamUtente);
        } else {
            setVisibilitaCampiTeam(false, null);
        }
    }

    private void setVisibilitaCampiTeam(boolean inTeam, Team teamUtente) {
        creaTeamButton.setVisible(!inTeam);
        nomeField.setVisible(!inTeam);
        nomeTeam.setVisible(!inTeam);
        uniscitiButton.setVisible(!inTeam);
        comboBox1.setVisible(!inTeam);
        teamLabel.setVisible(!inTeam);
        avviso.setVisible(!inTeam);
        messaggio.setVisible(inTeam);
        problema.setVisible(false);
        sfogliaDocumenti.setVisible(false);
        inserisciDocumento.setVisible(false);

        if (inTeam && teamUtente != null) {
            messaggio.setText("Ora sei membro del " + teamUtente.getNomeTeam());
            String problemaEventoInTeam = controller.getProblemaEvento(eventoId);
            if (problemaEventoInTeam != null && !problemaEventoInTeam.isEmpty()) {
                problema.setText(PROBLEMA_LABEL + problemaEventoInTeam);
                problema.setVisible(true);
                sfogliaDocumenti.setVisible(true);
                inserisciDocumento.setVisible(true);
            }
        }
    }

    private void mostraDocumentiTeam(JPanel documentiPanel, Team teamUtente) {
        documentiPanel.removeAll();
        List<Documento> documenti = controller.getDocumentiTeamEventoPartecipante(eventoId, teamUtente.getNomeTeam(), loginPartecipante);
        if (documenti.isEmpty()) {
            aggiungiLabel(documentiPanel, NESSUN_DOCUMENTO_LABEL, fieldFont);
        } else {
            for (Documento doc : documenti) {
                documentiPanel.add(creaDocPanel(doc));
                documentiPanel.add(Box.createVerticalStrut(8));
            }
        }
        documentiPanel.revalidate();
        documentiPanel.repaint();
    }

    private JPanel creaDocPanel(Documento doc) {
        JPanel docPanel = new JPanel();
        docPanel.setLayout(new BoxLayout(docPanel, BoxLayout.Y_AXIS));
        docPanel.setBackground(bgColor);
        docPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel rigaPanel = new JPanel();
        rigaPanel.setLayout(new BoxLayout(rigaPanel, BoxLayout.X_AXIS));
        rigaPanel.setBackground(bgColor);
        rigaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomeDocLabel = new JLabel(DOCUMENTO_CARICATO_LABEL + doc.getFile().getName());
        nomeDocLabel.setFont(fieldFont);

        JButton visualizzaDocButton = creaVisualizzaDocButton(doc);

        rigaPanel.add(nomeDocLabel);
        rigaPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rigaPanel.add(visualizzaDocButton);

        docPanel.add(rigaPanel);
        docPanel.add(creaCommentiPanel(doc.getCommentiGiudici()));
        return docPanel;
    }

    private JButton creaVisualizzaDocButton(Documento doc) {
        JButton visualizzaDocButton = new JButton("Visualizza");
        visualizzaDocButton.setBackground(btnColor);
        visualizzaDocButton.setForeground(Color.WHITE);
        visualizzaDocButton.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        visualizzaDocButton.setFocusPainted(false);
        visualizzaDocButton.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        visualizzaDocButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { visualizzaDocButton.setBackground(btnHoverColor); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { visualizzaDocButton.setBackground(btnColor); }
        });
        visualizzaDocButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(doc.getFile());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frameAreaPartecipante, "Impossibile aprire il documento.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        return visualizzaDocButton;
    }

    private JPanel creaCommentiPanel(List<CommentoGiudice> commenti) {
        JPanel commentiPanel = new JPanel();
        commentiPanel.setLayout(new BoxLayout(commentiPanel, BoxLayout.Y_AXIS));
        commentiPanel.setBackground(bgColor);
        commentiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (commenti == null || commenti.isEmpty()) {
            aggiungiLabel(commentiPanel, COMMENTO_GIUDICE_LABEL, fieldFont);
        } else {
            for (CommentoGiudice c : commenti) {
                aggiungiLabel(commentiPanel, c.getGiudice() + ": " + c.getTesto(), fieldFont);
            }
        }
        return commentiPanel;
    }

    private void aggiungiLabel(JPanel panel, String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    private void azioneCreaTeam(JPanel documentiPanel) {
        String nome = nomeField.getText();
        if (nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frameAreaPartecipante, "Inserire un nome valido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Team nuovoTeam = controller.creaTeam(nome, loginPartecipante, eventoId);
        controller.unisciPartecipanteATeam(loginPartecipante, nuovoTeam.getNomeTeam(), eventoId);
        comboBox1.addItem(nuovoTeam);

        Team nuovoTeamUtente = trovaTeamUtente(controller.getTeamsEvento(eventoId));
        aggiornaVisibilitaCampi(true, nuovoTeamUtente);
        mostraDocumentiTeam(documentiPanel, nuovoTeamUtente);
    }

    private void azioneHome() {
        ViewEvento gui = new ViewEvento(controller, loginPartecipante, frameAccedi, frameAreaPartecipante, frameNotifica, frameGiudice);
        gui.getFramePartecipante().setVisible(false);
        frameEventi.setVisible(true);
    }

    private void azioneUniscitiTeam(JPanel documentiPanel) {
        Team teamSelected = (Team) comboBox1.getSelectedItem();
        if (controller.isPartecipanteInTeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId)) {
            JOptionPane.showMessageDialog(frameAreaPartecipante, "Sei già in un team.");
            return;
        }
        if (controller.getDimTeam(teamSelected.getNomeTeam(), eventoId) >= controller.getEventoById(eventoId).getDMaxTeam()) {
            JOptionPane.showMessageDialog(frameAreaPartecipante, "Il team " + teamSelected.getNomeTeam() + " è pieno.");
            return;
        }
        controller.unisciPartecipanteATeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId);
        Team nuovoTeamUtente = trovaTeamUtente(controller.getTeamsEvento(eventoId));
        aggiornaVisibilitaCampi(true, nuovoTeamUtente);
        mostraDocumentiTeam(documentiPanel, nuovoTeamUtente);
    }

    private void azioneCaricaDocumento(JPanel documentiPanel) {
        JFileChooser fileChooser = new JFileChooser();
        int risultato = fileChooser.showOpenDialog(frameAreaPartecipante);
        if (risultato == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();

            Team teamUser = trovaTeamUtente(controller.getTeamsEvento(eventoId));
            Documento documento = new Documento(LocalDate.now(), file, teamUser);
            controller.caricaDocumento(documento, teamUser.getNomeTeam(), eventoId, loginPartecipante);
            JOptionPane.showMessageDialog(frameAreaPartecipante, "Documento caricato con successo!");

            mostraDocumentiTeam(documentiPanel, teamUser);
        }
    }
    public JFrame getFramePartecipante() {
        return frameAreaPartecipante;
    }
}
