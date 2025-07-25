package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


    public AreaPartecipante(String loginPartecipante, int idEvento, JFrame frameAreaEventi, JFrame frameAreaAccesso, JFrame frameAreaNotifiche, JFrame frameAreaGiudice, Controller controller) {
        this.controller = controller;
        this.loginPartecipante = loginPartecipante;
        this.eventoId = idEvento;
        frameEventi = frameAreaEventi;
        frameAccedi = frameAreaAccesso;
        frameNotifica = frameAreaNotifiche;
        frameGiudice = frameAreaGiudice;

        Partecipante partecipante = controller.getPartecipanteDaDB(loginPartecipante, eventoId);
        frameAreaPartecipante = new JFrame("Area Personale " + loginPartecipante);
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(600, 600);
        frameAreaPartecipante.setLocationRelativeTo(null);
        frameAreaPartecipante.setDefaultCloseOperation(EXIT_ON_CLOSE);

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

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewEvento gui = new ViewEvento(controller, loginPartecipante, frameAccedi, frameAreaPartecipante, frameNotifica, frameGiudice);
                gui.getFramePartecipante().setVisible(false);
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
    public JFrame getFramePartecipante() {
        return frameAreaPartecipante;
    }

}
