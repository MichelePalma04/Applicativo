package gui;

import controller.Controller;
import model.Evento;
import model.Utente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Invito {
    private JPanel panel;
    private JComboBox <Utente> comboBox1;
    private JLabel invitoLabel;
    private JButton invitaComeGiudiceButton;
    private JPanel panel2;
    private JLabel nomeEvento;
    private JButton homeButton;
    public static JFrame frameInvito, frameOrganizzatore;

    private Evento evento;

    public Invito(Evento evento, JFrame frame) {
        frameOrganizzatore = frame;
        this.evento = evento;
        frameInvito = new JFrame("Operazioni organizzatore");
        frameInvito.setContentPane(panel);
        frameInvito.pack();
        frameInvito.setSize(500, 500);
        frameInvito.setLocationRelativeTo(null);

        nomeEvento.setText("Evento " + evento.getTitolo());

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameInvito.setVisible(false);
                frameOrganizzatore.setVisible(true);
            }
        });

        invitaComeGiudiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utente daInvitare = (Utente) comboBox1.getSelectedItem();
                if (daInvitare != null) {
                    if (Controller.invitaGiudicePendente(evento, daInvitare)) {
                        JOptionPane.showMessageDialog(frameInvito, "Giudice invitato con sucesso!");
                        aggiornaListaInvitabili();
                    } else {
                        JOptionPane.showMessageDialog(frameInvito, "Errore, impossibile invitare l'utente");
                    }
                }
            }
        });
        aggiornaListaInvitabili();
        frameInvito.setVisible(true);
    }

    private void aggiornaListaInvitabili() {
        comboBox1.removeAllItems();
        for (Utente u : Controller.getUtentiInvitabili(evento)) {
            comboBox1.addItem(u);
        }
    }

}
