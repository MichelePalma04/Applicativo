package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public JFrame frameAreaPartecipante;
    public JFrame frameEventi;
    public JFrame frameAccedi;
    public JFrame frameNotifica;
    public JFrame frameGiudice;


    public AreaPartecipante(String loginPartecipante, int eventoId, JFrame frame, JFrame frame2, JFrame frame3, JFrame frame4, Controller controller) {
        this.controller = controller;
        this.loginPartecipante = loginPartecipante;
        this.eventoId = eventoId;
        frameEventi = frame;
        frameAccedi = frame2;
        frameNotifica = frame3;
        frameGiudice = frame4;

        Partecipante partecipante = controller.getPartecipanteDaDB(loginPartecipante, eventoId);
        frameAreaPartecipante = new JFrame("Area Personale " + loginPartecipante);
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(600, 600);
        frameAreaPartecipante.setLocationRelativeTo(null);

        messaggio.setVisible(false);

        avviso.setText("Iscrizione avvenuta con successo!");
        benvenuto.setText("Benvenuto, " + loginPartecipante);


        List<Team> teams = controller.getTeamsEvento(eventoId);
        if (teams.isEmpty()) {
            uniscitiButton.setEnabled(false);
            comboBox1.setEnabled(false);
            teamLabel.setText("Nessun team disponibile a cui unirsi.");
        } else {
            for (Team team : teams) {
                comboBox1.addItem(team);
            }
        }

        // boolean inTeam = false;
        // Verifica se il partecipante è già in un team
        Team teamUtente = null;
        for (Team team : teams) {
            if (controller.isPartecipanteInTeam(loginPartecipante, team.getNomeTeam(), eventoId)) {
                //inTeam = true;
                teamUtente = team;
                break;
            }
        }

        boolean inTeam = (teamUtente != null);
        String problemaEvento = controller.getProblemaEvento(eventoId);
        JPanel documentiPanel = new JPanel();
        documentiPanel.setLayout(new BoxLayout(documentiPanel, BoxLayout.Y_AXIS));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS)); // esempio
        panel2.add(documentiPanel);

        if (inTeam) {
            creaTeamButton.setVisible(false);
            nomeField.setVisible(false);
            nomeTeam.setVisible(false);
            uniscitiButton.setVisible(false);
            comboBox1.setVisible(false);
            teamLabel.setVisible(false);
            avviso.setVisible(false);
            messaggio.setVisible(true);
            messaggio.setText("Ora sei membro del " + teamUtente.getNomeTeam());
            String problemaEventoInTeam = controller.getProblemaEvento(eventoId);
            if (problemaEventoInTeam != null && !problemaEventoInTeam.isEmpty()) {
                problema.setText("Problema da risolvere: " + problemaEventoInTeam);
                problema.setVisible(true);
                sfogliaDocumenti.setVisible(true);
                inserisciDocumento.setVisible(true);
            } else {
                problema.setVisible(false);
                sfogliaDocumenti.setVisible(false);
                inserisciDocumento.setVisible(false);
            }

            // --- Visualizzazione documenti e commenti ---
            documentiPanel.removeAll();
            List<Documento> documenti = controller.getDocumentiTeamEventoPartecipante(eventoId, teamUtente.getNomeTeam(), loginPartecipante);
            if (documenti.isEmpty()) {
                JLabel noDocLabel = new JLabel("Nessun documento caricato dal tuo team.");
                documentiPanel.add(noDocLabel);
            } else {
                documentiPanel.removeAll();
                documentiPanel.setLayout(new BoxLayout(documentiPanel, BoxLayout.Y_AXIS));
                for (Documento doc : documenti) {
                    JPanel docPanel = new JPanel();
                    docPanel.setLayout(new BoxLayout(docPanel, BoxLayout.Y_AXIS));
                    JPanel rigaPanel = new JPanel();
                    rigaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    JLabel nomeDocLabel = new JLabel("Documento caricato: " + doc.getFile().getName());
                    //rigaPanel.add(nomeDocLabel);

                    JButton visualizzaDocButton = new JButton("Visualizza");
                    visualizzaDocButton.addActionListener(e -> {
                        try {
                            Desktop.getDesktop().open(doc.getFile());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frameAreaPartecipante, "Impossibile aprire il documento.", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    rigaPanel.add(nomeDocLabel);
                    rigaPanel.add(visualizzaDocButton);
                    docPanel.add(rigaPanel);

                   /* StringBuilder commentiSb = new StringBuilder("<html>");
                    List<CommentoGiudice> commenti = doc.getCommentiGiudici();
                    if (commenti == null || commenti.isEmpty()) {
                        commentiSb.append("Nessun commento dai giudici.");
                    } else {
                        for (CommentoGiudice c : commenti) {
                            commentiSb.append("<b>")
                                    .append(c.getNomeGiudice())
                                    .append(":</b> ")
                                    .append(c.getTesto())
                                    .append("<br>");
                        }
                    }
                    commentiSb.append("</html>");
                    JLabel commentiLabel = new JLabel(commentiSb.toString());
                    commentiLabel.setVerticalAlignment(SwingConstants.TOP);
                    docPanel.add(commentiLabel);

                    */
                    List<CommentoGiudice> commenti = doc.getCommentiGiudici();
                    if (commenti == null || commenti.isEmpty()) {
                        JLabel nessunCommento = new JLabel("Nessun commento dai giudici.");
                        docPanel.add(nessunCommento);
                    } else {
                        for (CommentoGiudice c : commenti) {
                            JLabel commentoLabel = new JLabel(c.getGiudice() + ": " + c.getTesto()+"\n");
                            commentoLabel.setFont(commentoLabel.getFont().deriveFont(Font.PLAIN));
                            //commentoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);// Puoi personalizzare il font qui
                            docPanel.add(commentoLabel);
                        }
                    }
                    documentiPanel.add(docPanel);
                }
            }
            documentiPanel.revalidate();
            documentiPanel.repaint();
        } else {
            uniscitiButton.setVisible(true);
            comboBox1.setVisible(true);
            teamLabel.setVisible(true);
            avviso.setVisible(true);
            messaggio.setVisible(false);
            problema.setVisible(false);
            sfogliaDocumenti.setVisible(false);
            inserisciDocumento.setVisible(false);
        }

        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                if (nome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Inserire un nome valido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Crea il team nel DB e unisci subito il partecipante
                Team nuovoTeam = controller.creaTeam(nome, loginPartecipante, eventoId);
                controller.unisciPartecipanteATeam(loginPartecipante, nuovoTeam.getNomeTeam(), eventoId);
                comboBox1.addItem(nuovoTeam);

                //Evento eventoAggiornato = controller.geteventoById(eventoId);

                List<Team> teamsAggiornati = controller.getTeamsEvento(eventoId);
                Team nuovoTeamUtente = null;
                for (Team t : teamsAggiornati) {
                    if (controller.isPartecipanteInTeam(loginPartecipante, t.getNomeTeam(), eventoId)) {
                        nuovoTeamUtente = t;
                        break;
                    }
                }

                boolean inTeamAggiornato = (nuovoTeamUtente != null);
                String problemaEvento = controller.getProblemaEvento(eventoId);

                if (inTeamAggiornato) {
                    nomeField.setVisible(false);
                    nomeTeam.setVisible(false);
                    creaTeamButton.setVisible(false);
                    teamLabel.setVisible(false);
                    comboBox1.setVisible(false);
                    uniscitiButton.setVisible(false);
                    avviso.setVisible(false);
                    messaggio.setVisible(true);
                    messaggio.setText("Ora sei un membro del team " + nuovoTeamUtente.getNomeTeam());
                    if (problemaEvento != null && !problemaEvento.isEmpty()) {
                        problema.setText("Problema da risolvere: " + problemaEvento);
                        problema.setVisible(true);
                        sfogliaDocumenti.setVisible(true);
                        inserisciDocumento.setVisible(true);
                    } else {
                        problema.setVisible(false);
                        sfogliaDocumenti.setVisible(false);
                        inserisciDocumento.setVisible(false);
                    }
                    // DEBUG VISIBILITA' DOPO CREA TEAM
                    System.out.println("DOPO CREA TEAM - VISIBILITA':");
                    System.out.println("Problema visibile: " + problema.isVisible());
                    System.out.println("SfogliaDocumenti visibile: " + sfogliaDocumenti.isVisible());
                    System.out.println("InserisciDocumento visibile: " + inserisciDocumento.isVisible());
                }
            }
        });

        if (inTeam) {
            creaTeamButton.setVisible(false);
            nomeField.setVisible(false);
            nomeTeam.setVisible(false);
            uniscitiButton.setVisible(false);
            comboBox1.setVisible(false);
            teamLabel.setVisible(false);
            avviso.setVisible(false);
            messaggio.setVisible(true);
            messaggio.setText("Ora sei membro del " + teamUtente.getNomeTeam());
            String problemaEventoInTeam = controller.getProblemaEvento(eventoId);
            if (problemaEventoInTeam != null && !problemaEventoInTeam.isEmpty()) {
                problema.setText("Problema da risolvere: " + problemaEventoInTeam);
                problema.setVisible(true);
                sfogliaDocumenti.setVisible(true);
                inserisciDocumento.setVisible(true);
            } else {
                problema.setVisible(false);
                sfogliaDocumenti.setVisible(false);
                inserisciDocumento.setVisible(false);
            }
            System.out.println("INTEAM - VISIBILITA' INIZIALE:");
            System.out.println("Problema visibile: " + problema.isVisible());
            System.out.println("SfogliaDocumenti visibile: " + sfogliaDocumenti.isVisible());
            System.out.println("InserisciDocumento visibile: " + inserisciDocumento.isVisible());
        } else {
            uniscitiButton.setVisible(true);
            comboBox1.setVisible(true);
            teamLabel.setVisible(true);
            avviso.setVisible(true);
            messaggio.setVisible(false);
            problema.setVisible(false);
            sfogliaDocumenti.setVisible(false);
            inserisciDocumento.setVisible(false);
            // DEBUG VISIBILITA' INIZIALE DA ELSE
            System.out.println("NON INTEAM - VISIBILITA' INIZIALE:");
            System.out.println("Problema visibile: " + problema.isVisible());
            System.out.println("SfogliaDocumenti visibile: " + sfogliaDocumenti.isVisible());
            System.out.println("InserisciDocumento visibile: " + inserisciDocumento.isVisible());
        }

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewEvento gui = new ViewEvento(controller, loginPartecipante, frameAccedi, frameAreaPartecipante, frameNotifica, frameGiudice);
                gui.frameAreaPartecipante.setVisible(false);
                frameEventi.setVisible(true);
            }
        });

        uniscitiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Team teamSelected = (Team) comboBox1.getSelectedItem();

                // Verifica se già in un team tramite Controller
                if (controller.isPartecipanteInTeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId)) {
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Sei già in un team.");
                    return;
                }

                // Verifica la dimensione massima tramite Controller
                String problemaEvento = controller.getProblemaEvento(eventoId);
                if (controller.getDimTeam(teamSelected.getNomeTeam(), eventoId) >= controller.getEventoById(eventoId).getDMaxTeam()) {
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Il team " + teamSelected.getNomeTeam() + " è pieno.");
                    return;
                }
                controller.unisciPartecipanteATeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId);

                // Aggiorna la visibilità dopo l'unione
                List<Team> teamsAggiornati = controller.getTeamsEvento(eventoId);
                Team nuovoTeamUtente = null;
                for (Team t : teamsAggiornati) {
                    if (controller.isPartecipanteInTeam(loginPartecipante, t.getNomeTeam(), eventoId)) {
                        nuovoTeamUtente = t;
                        break;
                    }
                }
                boolean inTeamAggiornato = (nuovoTeamUtente != null);
                String problemaEventoAggiornato = controller.getProblemaEvento(eventoId);
                System.out.println("DOPO UNISCI TEAM:");
                System.out.println("inTeamAggiornato: " + inTeamAggiornato);
                System.out.println("problema: " + (problemaEventoAggiornato != null ? problemaEventoAggiornato : "null"));

                if (inTeamAggiornato) {
                    creaTeamButton.setVisible(false);
                    nomeField.setVisible(false);
                    nomeTeam.setVisible(false);
                    comboBox1.setVisible(false);
                    teamLabel.setVisible(false);
                    uniscitiButton.setVisible(false);
                    avviso.setVisible(false);
                    messaggio.setVisible(true);
                    messaggio.setText("Ora sei un membro del " + nuovoTeamUtente.getNomeTeam());
                    if (problemaEventoAggiornato != null && !problemaEventoAggiornato.isEmpty()) {
                        problema.setText("Problema da risolvere: " + problemaEventoAggiornato);
                        problema.setVisible(true);
                        sfogliaDocumenti.setVisible(true);
                        inserisciDocumento.setVisible(true);
                    } else {
                        problema.setVisible(false);
                        sfogliaDocumenti.setVisible(false);
                        inserisciDocumento.setVisible(false);
                    }
                    // DEBUG VISIBILITA' DOPO UNISCI TEAM
                    System.out.println("DOPO UNISCI TEAM - VISIBILITA':");
                    System.out.println("Problema visibile: " + problema.isVisible());
                    System.out.println("SfogliaDocumenti visibile: " + sfogliaDocumenti.isVisible());
                    System.out.println("InserisciDocumento visibile: " + inserisciDocumento.isVisible());

/*
                // Unisci il partecipante al team tramite Controller
                controller.unisciPartecipanteATeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId);
                creaTeamButton.setVisible(false);
                nomeField.setVisible(false);
                nomeTeam.setVisible(false);
                comboBox1.setVisible(false);
                teamLabel.setVisible(false);
                uniscitiButton.setVisible(false);
                avviso.setVisible(false);
                messaggio.setVisible(true);
                messaggio.setText("Ora sei un membro del " + teamSelected.getNomeTeam());
                if(evento.getProblema()!= null && !evento.getProblema().isEmpty()){
                    problema.setText("Problema da risolvere: " + evento.getProblema());
                    problema.setVisible(true);
                    sfogliaDocumenti.setVisible(true);
                    inserisciDocumento.setVisible(true);
                }else{
                    problema.setVisible(false);
                    sfogliaDocumenti.setVisible(false);
                    inserisciDocumento.setVisible(false);*/
                }
            }
        });


        sfogliaDocumenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int risultato = fileChooser.showOpenDialog(frameAreaPartecipante);
                if (risultato == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileChooser.getSelectedFile();

                    //Trova il team dell'utente tramite controller
                    Team teamUser = null;
                    List<Team> teams = controller.getTeamsEvento(eventoId);
                    for (Team t : teams) {
                        if (controller.isPartecipanteInTeam(loginPartecipante, t.getNomeTeam(), eventoId)) {
                            teamUser = t;
                            break;
                        }
                    }
                    Documento documento = new Documento(LocalDate.now(), file, teamUser);
                    controller.caricaDocumento(documento, teamUser.getNomeTeam(), eventoId, loginPartecipante);
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Documento caricato con successo!");
                  //  --- AGGIUNGI QUESTO BLOCCO PER AGGIORNARE LA LISTA DOCUMENTI ---
                            // Recupera di nuovo la lista dei documenti del team
                            List<Documento> documentiAggiornati = controller.getDocumentiTeamEventoPartecipante(eventoId, teamUser.getNomeTeam(), loginPartecipante);

                    documentiPanel.removeAll(); // pulisci il pannello
                    if (documentiAggiornati.isEmpty()) {
                        JLabel noDocLabel = new JLabel("Nessun documento caricato dal tuo team.");
                        documentiPanel.add(noDocLabel);
                    } else {
                        for (Documento doc : documentiAggiornati) {
                            JPanel docPanel = new JPanel();
                            docPanel.setLayout(new BoxLayout(docPanel, BoxLayout.Y_AXIS));
                            JPanel rigaPanel = new JPanel();
                            rigaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                            JLabel nomeDocLabel = new JLabel("Documento caricato: " + doc.getFile().getName());

                            JButton visualizzaDocButton = new JButton("Visualizza");
                            visualizzaDocButton.addActionListener(ev -> {
                                try {
                                    Desktop.getDesktop().open(doc.getFile());
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Impossibile aprire il documento.", "Errore", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                            rigaPanel.add(nomeDocLabel);
                            rigaPanel.add(visualizzaDocButton);
                            docPanel.add(rigaPanel);

                            List<CommentoGiudice> commenti = doc.getCommentiGiudici();
                            if (commenti == null || commenti.isEmpty()) {
                                JLabel nessunCommento = new JLabel("Nessun commento dai giudici.");
                                docPanel.add(nessunCommento);
                            } else {
                                for (CommentoGiudice c : commenti) {
                                    JLabel commentoLabel = new JLabel(c.getGiudice() + ": " + c.getTesto());
                                    commentoLabel.setFont(commentoLabel.getFont().deriveFont(Font.PLAIN));
                                    docPanel.add(commentoLabel);
                                }
                            }
                            documentiPanel.add(docPanel);
                        }
                    }
                    documentiPanel.revalidate();
                    documentiPanel.repaint();
                    // --- FINE BLOCCO AGGIORNAMENTO ---
                }
            }
        });
    }


}
