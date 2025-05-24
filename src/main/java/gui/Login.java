package gui;

import model .*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;

public class Login {
    private JPanel Login;
    private JPanel PanneloMail;
    private JLabel mailLabel;
    private JLabel passwordLabel;
    private JTextField mailField;
    private JPasswordField passwordField;
    private JButton accediButton;
    private JButton registratiButton;
    private JPanel panel;
    public static JFrame frame, frame2, frameEventi;
    private Controller controller;

    public Login() {
        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = new String(mailField.getText());
                String password = new String(passwordField.getText());
                Utente u = Controller.loginUtente(mail, password);
                if (u != null) {
                    if (u instanceof Organizzatore) {
                        AreaOrganizzatore QuartaGUI = new AreaOrganizzatore(controller,(Organizzatore) u, frame);
                        QuartaGUI.frameOrganizzatore.setVisible(true);
                        frame.dispose();
                    }else {
                        ViewEvento terzaGUI = new ViewEvento(controller, frame, frame2);
                        terzaGUI.frameEventi.setVisible(true);
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun utente trovato, effettua prima la registrazione se non ancora lo hai fatto.");
                }
            }
        });

        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrazione secondGUI = new Registrazione(controller, frame);
                secondGUI.frameRegistrazione.setVisible(true);
                frame.setVisible(false);
            }
        });
    }
    public static void main (String[]args){
        Controller.initEventi();
        frame = new JFrame("Login");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}