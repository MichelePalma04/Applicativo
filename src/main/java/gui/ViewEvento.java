package gui;

import controller.Controller;
import model.Evento;
import model.Partecipante;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewEvento {
    private JPanel ViewEvento;
    private JPanel panelEventi;
   /* private JScrollPane scroll;
    private Controller controller;


    public static JFrame frameEventi, frameAccedi;

    public ViewEvento(Controller controller, JFrame frame) {
        scroll.getVerticalScrollBar().setUnitIncrement(20); //abbiamo aumentato la sensibilità dello scroll
        frameAccedi = frame;
        this.controller = controller;
        Controller.initEventi();
        ArrayList<Evento> eventi = Controller.getEventiDisponibili();

        frameEventi = new JFrame("Eventi");
        frameEventi.setContentPane(ViewEvento);
        frameEventi.pack();
        frameEventi.setSize(500,500);
        frameEventi.setLocationRelativeTo(null);

        panelEventi.setLayout(new BoxLayout(panelEventi, BoxLayout.Y_AXIS));

        for (Evento ev : eventi) {
            JPanel eventoPanel = new JPanel(new GridLayout(0,1));
            eventoPanel.setBorder(BorderFactory.createTitledBorder(ev.getTitolo()));

            eventoPanel.add(new JLabel("Sede: " + ev.getSede()));
            eventoPanel.add(new JLabel("Data Inizio: " + ev.getDataInizio()));
            eventoPanel.add(new JLabel("Data Fine: " + ev.getDataFine()));
            eventoPanel.add(new JLabel("Data apertura registrazione: " +ev.getInizio_registrazioni()));
            eventoPanel.add(new JLabel("Data fine registrazione: " +ev.getFine_registrazioni()));
            eventoPanel.add(new JLabel("Organizzatore: " + ev.getOrganizzatore().getLogin()));
            JButton iscrivitiButton = new JButton("Iscriviti");
            eventoPanel.add(iscrivitiButton);

            iscrivitiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Utente u = Controller.getUtenteCorrente();
                    for (Partecipante p: ev.getPartecipanti()) {
                        if (p.getLogin().equals(u.getLogin())) {
                            JOptionPane.showMessageDialog(frameEventi, "Sei già iscritto a questo evento.");
                            return;
                        }
                    }
                    Partecipante partecipanteEsistente = null;
                    for (Evento altroEvento : Controller.getEventiDisponibili()) {
                        for (Partecipante p : altroEvento.getPartecipanti()) {
                            if (p.getLogin().equals(u.getLogin())) {
                                partecipanteEsistente = p;
                                break;
                            }
                        }
                        if (partecipanteEsistente != null) break;
                    }

                    if (partecipanteEsistente != null) {
                        partecipanteEsistente.getEventi().add(ev);
                        ev.getPartecipanti().add(partecipanteEsistente);
                    } else {
                        ArrayList<Evento> eventiPartecipante = new ArrayList<>();
                        eventiPartecipante.add(ev);
                        Partecipante nuovo = new Partecipante(u.getLogin(), u.getPassword(), null, eventiPartecipante);
                        ev.getPartecipanti().add(nuovo);
                    }
                    JOptionPane.showMessageDialog(frameEventi, "Iscrizione completata con successo.");

                }

            });
            panelEventi.add(eventoPanel);
        }

        panelEventi.revalidate();
        panelEventi.repaint();

    }
    */
    

}
