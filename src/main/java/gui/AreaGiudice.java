package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

public class AreaGiudice {
    private JPanel mainPanel;
    private JButton logOutButton;
    private JLabel benvenuto;
    private JScrollPane scroll;
    private JPanel panel;
    private JButton back;
    public JFrame frameGiudice;
    public JFrame frameAccesso;
    public JFrame frameEventi;
    private String loginGiudice;
    private int eventoId;

    private Controller controller;

    public AreaGiudice(Controller controller, String loginGiudice, int eventoId, JFrame frame, JFrame eventi) {
        this.controller = controller;
        this.loginGiudice = loginGiudice;
        this.eventoId = eventoId;
        frameAccesso = frame;
        frameEventi = eventi;

        //Giudice giudice = controller.getGiudiceEvento(loginGiudice, eventoId);
        //Evento eventoCorrente = controller.geteventoById(eventoId);
        benvenuto.setText("Benvenuto "+ loginGiudice);
        frameGiudice = new JFrame();
        frameGiudice.setTitle("Area giudice "+ loginGiudice);
        frameGiudice.setContentPane(mainPanel);
        frameGiudice.pack();
        frameGiudice.setSize(500,500);
        frameGiudice.setLocationRelativeTo(null);

        scroll.getVerticalScrollBar().setUnitIncrement(20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String problema = controller.getProblemaEvento(eventoId);
        Giudice responsabile = controller.getGiudiceDescrizione(eventoId);

        // 1. Se il giudice è responsabile, può inserire il problema se non c'è
        if (responsabile != null && loginGiudice.equals(responsabile.getLogin())) {
            JLabel messaggio = new JLabel("Sei il giudice responsabile della descrizione del problema.");
            if (problema == null || problema.isEmpty()) {
                JTextArea descrizioneProblema = new JTextArea(5, 20);
                JScrollPane scrollProblema = new JScrollPane(descrizioneProblema);
                JButton carica = new JButton("Carica problema");

                panel.add(messaggio);
                panel.add(scrollProblema);
                panel.add(carica);

                carica.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String testo = descrizioneProblema.getText();
                        if (testo.isEmpty()) {
                            JOptionPane.showMessageDialog(frameGiudice, "Inserisci la descrizione del problema prima di effettuare il caricamento!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Salva nel DB tramite Controller!
                            controller.setProblemaEvento(eventoId, testo);
                            JOptionPane.showMessageDialog(frameGiudice, "Descrizione del problema caricata con successo!");
                            panel.removeAll();
                            JLabel giaCaricata = new JLabel("Problema:\n " + testo);
                            panel.add(messaggio);
                            panel.add(giaCaricata);
                            panel.revalidate();
                            panel.repaint();
                        }
                    }
                });
            } else {
                JLabel giaCaricata = new JLabel("Problema:\n " + problema);
                panel.add(messaggio);
                panel.add(giaCaricata);
            }
        } else if (problema != null && !problema.isEmpty()) {
            // 2. Gli altri giudici vedono solo il testo del problema
            JLabel message = new JLabel("Problema assegnato per l'evento: \n" + problema);
            panel.add(message);
        } else {
            // 3. Nessun problema inserito
            JLabel noProblem = new JLabel("Nessun problema inserito per questo evento.");
            panel.add(noProblem);
        }

        // 4. Documenti caricati dai team (direttamente da DB)
        List<Team> teams = controller.getTeamsEvento(eventoId);
        if (teams != null && !teams.isEmpty()) {
            JLabel documentiLabel = new JLabel("Documenti caricati dai team: ");
            panel.add(documentiLabel);

            for (Team t : teams) {
                boolean haDocumenti = controller.teamHaDocumenti(t.getNomeTeam(), eventoId);
                if (haDocumenti) {
                    JLabel nomeTeam = new JLabel(t.getNomeTeam());
                    JButton visualizzaDocumenti = new JButton("Visualizza documenti");
                    JPanel rigaTeam = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    boolean haVotato = controller.giudiceHaVotatoTeam(loginGiudice, t.getNomeTeam(), eventoId);
                    if (haVotato) {
                        int votoAssegnato = controller.getVotoDiGiudiceTeam(loginGiudice, t.getNomeTeam(), eventoId);
                        rigaTeam.add(nomeTeam);
                        rigaTeam.add(visualizzaDocumenti);
                        rigaTeam.add(new JLabel("Voto al team: " + votoAssegnato));
                        panel.add(rigaTeam);
                    } else {
                        JComboBox<Integer> voti = new JComboBox<>();
                        for (int i = 0; i <= 10; i++) {
                            voti.addItem(i);
                        }
                        JButton confermaVoto = new JButton("Conferma");
                        rigaTeam.add(nomeTeam);
                        rigaTeam.add(visualizzaDocumenti);
                        rigaTeam.add(new JLabel("Voto: "));
                        rigaTeam.add(voti);
                        rigaTeam.add(confermaVoto);
                        panel.add(rigaTeam);

                        confermaVoto.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int votoSelezionato = (int) voti.getSelectedItem();
                                // Registra il voto tramite Controller/DB!
                                controller.votaTeam(loginGiudice, t.getNomeTeam(), eventoId, votoSelezionato);
                                JOptionPane.showMessageDialog(frameGiudice, "Voto " + votoSelezionato + " assegnato con successo al team " + t.getNomeTeam());
                                voti.setVisible(false);
                                confermaVoto.setVisible(false);
                                JLabel votoAssegnato = new JLabel("" + votoSelezionato);
                                rigaTeam.add(votoAssegnato);
                                rigaTeam.revalidate();
                                rigaTeam.repaint();
                            }
                        });
                    }

                    visualizzaDocumenti.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            VisualizzaDocumenti nuovaGUI = new VisualizzaDocumenti(controller, t.getNomeTeam(), eventoId, frameGiudice);
                            nuovaGUI.frameDocumenti.setVisible(true);
                            frameGiudice.setVisible(false);
                        }
                    });
                }
            }
        } else {
            JLabel noDocumenti = new JLabel("Nessun documento caricato dai team.");
            panel.add(noDocumenti);
        }

        /*if(eventoCorrente.getGiudiceDescrizione() != null && eventoCorrente.getGiudiceDescrizione().getLogin().equals(loginGiudice)) {
            JLabel messaggio = new JLabel("Sei il giudice responsabile della descrizione del problema.");
            if (eventoCorrente.getProblema() == null || eventoCorrente.getProblema().isEmpty()) {
                JTextArea descrizioneProblema = new JTextArea(5, 20);
                JScrollPane scrollProblema = new JScrollPane(descrizioneProblema);
                JButton carica = new JButton("Carica problema");

                panel.add(messaggio);
                panel.add(scrollProblema);
                panel.add(carica);

                carica.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String testo = descrizioneProblema.getText();
                        if (testo.isEmpty()) {
                            JOptionPane.showMessageDialog(frameGiudice, "Inserisci la descrizione del problema prima di effettuare il caricamento!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            eventoCorrente.setProblema(testo);
                            JOptionPane.showMessageDialog(frameGiudice, "Descrizione del problema caricata con successo!");
                            panel.removeAll();
                            JLabel giaCaricata = new JLabel("Problema:\n " + eventoCorrente.getProblema());
                            panel.add(messaggio);
                            panel.add(giaCaricata);
                            panel.revalidate();
                            panel.repaint();
                        }
                    }
                });
            } else {
                JLabel giaCaricata = new JLabel("Problema:\n " + eventoCorrente.getProblema());
                panel.add(giaCaricata);
            }
        }

        if(eventoCorrente.getProblema() != null && !eventoCorrente.getProblema().isEmpty() && !giudice.equals(eventoCorrente.getGiudiceDescrizione())) {
            JLabel message = new JLabel("Problema assegnato per l'evento: \n" + eventoCorrente.getProblema());

            panel.add(message);

        }

        if (eventoCorrente.getDocumenti() != null && !eventoCorrente.getDocumenti().isEmpty()) {
            JLabel documenti = new JLabel("Documenti caricati dai team: ");
            panel.add (documenti);

            for (Team t: eventoCorrente.getTeams()){
                boolean haDocumenti = false;
                for (Documento doc: eventoCorrente.getDocumenti()){
                    if(doc.getTeam().equals(t)){
                        haDocumenti = true;
                        break;
                    }
                }

                if(haDocumenti){
                    JLabel nomeTeam = new JLabel(t.getNomeTeam());
                    JButton visualizzaDocumenti = new JButton("Visualizza documenti");
                    JPanel rigaTeam = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    if(t.haVotato(giudice)){
                        int votoAssegnato = t.getVotoDiGiudici(giudice);
                        rigaTeam.add(nomeTeam);
                        rigaTeam.add(visualizzaDocumenti);
                        rigaTeam.add(new JLabel("Voto al team: "+ votoAssegnato));
                        panel.add(rigaTeam);
                    }else{
                        JComboBox <Integer> voti = new JComboBox<>();
                        for(int i = 0; i <= 10; i++){
                            voti.addItem(i);
                        }
                        JButton confermaVoto = new JButton("Conferma");
                        rigaTeam.add(nomeTeam);
                        rigaTeam.add(visualizzaDocumenti);
                        rigaTeam.add(new JLabel("Voto: "));
                        rigaTeam.add(voti);
                        rigaTeam.add(confermaVoto);
                        panel.add(rigaTeam);

                        confermaVoto.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int votoSelezionato = (int) voti.getSelectedItem();
                                t.setVotoDiGiudice(giudice, votoSelezionato);
                                JOptionPane.showMessageDialog(frameGiudice, "Voto "+ votoSelezionato + " assegnato con successo al team "+ t.getNomeTeam());
                                //voto.setVisible(false);
                                voti.setVisible(false);
                                confermaVoto.setVisible(false);
                                JLabel votoAssegnato = new JLabel("" +votoSelezionato);
                                rigaTeam.add(votoAssegnato);

                                rigaTeam.revalidate();
                                rigaTeam.repaint();
                            }
                        });
                    }

                    visualizzaDocumenti.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                           VisualizzaDocumenti nuovaGUI = new VisualizzaDocumenti(controller, t, eventoCorrente, frameGiudice);
                           nuovaGUI.frameDocumenti.setVisible(true);
                           frameGiudice.setVisible(false);
                        }
                    });


                }
            }
        }else{
            JLabel noDocumenti = new JLabel ("Nessun documento caricato dai team.");
            panel.add (noDocumenti);
        }

         */




        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameGiudice.dispose();
                frameAccesso.setVisible(true);
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameGiudice.dispose();
                frameEventi.setVisible(true);
            }
        });
    }
}
