package gui;

import controller.Controller;
import model.Evento;
import model.Partecipante;

import javax.swing.*;

public class AreaPartecipante {
    private JLabel avviso;
    private JButton homeButton;
    private JComboBox comboBox1;
    private JButton uniscitiButton;
    private JLabel benvenuto;
    private JLabel team;
    private JPanel panel;
    private Controller controller;
    public static JFrame frameAreaPartecipante, frameEventi;

    public AreaPartecipante(Partecipante partecipante, Evento evento, JFrame frame) {
        frameEventi = frame;
        frameAreaPartecipante = new JFrame("Area Personale " + partecipante.getLogin());
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(500, 500);
        frameAreaPartecipante.setLocationRelativeTo(null);

        avviso.setText("Iscrizione avvenuta con successo!");
        benvenuto.setText("Benvenuto, " + partecipante.getLogin());

    }

}
