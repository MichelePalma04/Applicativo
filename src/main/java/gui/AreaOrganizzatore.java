package gui;

import controller.Controller;
import model.Organizzatore;

import javax.swing.*;

public class AreaOrganizzatore {
    private JPanel panel;
    private JLabel areaField;
    public static JFrame frameOrganizzatore, frameAccessi;
    private Controller controller;


    public AreaOrganizzatore(Organizzatore organizzatore){

        frameOrganizzatore = new JFrame("Area Organizzatore");
        frameOrganizzatore.setContentPane(panel);
        frameOrganizzatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameOrganizzatore.pack();
        frameOrganizzatore.setSize(500, 500);
        frameOrganizzatore.setLocationRelativeTo(null);
        frameOrganizzatore.setVisible(true);
    }


}
