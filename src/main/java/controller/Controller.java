package controller;
import model .*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final List<Utente> utentiRegistrati = new ArrayList<Utente>();
    private static final ArrayList<Evento> eventiDisponibili = new ArrayList();


    public static void initEventi() {
        ArrayList<Giudice> giudici = new ArrayList<>();
        ArrayList<Evento> eventiOrganizzati = new ArrayList<>();
        ArrayList<Partecipante> partecipanti = new ArrayList<>();

        Organizzatore o =new Organizzatore("admin", "admin", eventiOrganizzati, giudici);

        Evento e = new Evento("Hackathon", "Faggiano",
                                LocalDate.of(2025, 6, 9), LocalDate.of(2025, 6, 11),
                                40, 3, LocalDate.of(2025, 6, 3), LocalDate.of(2025, 6, 7),
                                o, giudici, partecipanti );
        Evento e1 = new Evento("Hackaton-Speed", "Puccianiello",
                                LocalDate.of(2025, 11, 20), LocalDate.of(2025, 11, 22),
                                 30, 5, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 18),
                                o, giudici, partecipanti);
        Evento e2 = new Evento("Hackaton-Speed", "Roma",
                LocalDate.of(2025, 11, 20), LocalDate.of(2025, 11, 22),
                30, 5, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 18),
                o, giudici,  partecipanti);

        eventiDisponibili.add(e);
        eventiDisponibili.add(e1);
        eventiDisponibili.add(e2);
        eventiOrganizzati.add(e);
        eventiOrganizzati.add(e1);
        eventiOrganizzati.add(e2);
    }

    public static ArrayList<Evento> getEventiDisponibili() {
        return eventiDisponibili;
    }

    public static boolean registraUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email)){
                return false; //utente già presente
            }
        }
        utentiRegistrati.add(new Utente(email, password));
        return true;
    }

    public static Utente loginUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
