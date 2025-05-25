package controller;
import model .*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final List<Utente> utentiRegistrati = new ArrayList<Utente>();
    private static final ArrayList<Evento> eventiDisponibili = new ArrayList();
    private static Utente utenteCorrente = null;
    private static Partecipante partecipantCorrente = null;
    private static ArrayList<InvitoGiudice> invitiPendenti = new ArrayList<>();
    //private static final ArrayList <Team> teamDisponibili = new ArrayList();


    public static void initEventi() {
        ArrayList<Giudice> giudici = new ArrayList<>();
        ArrayList<Evento> eventiOrganizzati = new ArrayList<>();
        ArrayList<Evento> eventiOrganizzati2 = new ArrayList<>();
        ArrayList<Partecipante> partecipanti = new ArrayList<>();
        ArrayList <Partecipante> partecipanti2 = new ArrayList();
        ArrayList<Partecipante> partecipanti3 = new ArrayList();
        ArrayList<Partecipante> partecipante1Team1 = new ArrayList<>();
        ArrayList <Partecipante> partecipante2Team1 = new ArrayList();
        ArrayList<Partecipante> partecipante3Team1 = new ArrayList();
        ArrayList<Partecipante> partecipante1Team2 = new ArrayList<>();
        ArrayList <Partecipante> partecipante2Team2 = new ArrayList();
        ArrayList<Partecipante> partecipante3Team2 = new ArrayList();
        ArrayList<Partecipante> partecipante1Team3 = new ArrayList<>();
        ArrayList <Partecipante> partecipante2Team3 = new ArrayList();
        ArrayList<Partecipante> partecipante3Team3 = new ArrayList();
        ArrayList <Team> teams = new ArrayList();
        ArrayList <Team> teams1 = new ArrayList();
        ArrayList <Team> teams2 = new ArrayList();
        teams.add(new Team ("Team 1",partecipante1Team1, null));
        teams.add(new Team ("Team 2",partecipante2Team1, null));
        teams.add(new Team ("Team 3",partecipante3Team1, null));
        teams1.add(new Team ("Team A",partecipante1Team2, null));
        teams1.add(new Team ("Team B",partecipante2Team2, null));
        teams1.add(new Team ("Team C",partecipante3Team2, null));
        teams2.add(new Team ("Team X",partecipante1Team3, null));
        teams2.add(new Team ("Team Y",partecipante2Team3, null));
        teams2.add(new Team ("Team Z",partecipante3Team3, null));

        Organizzatore o =new Organizzatore("miki&sara", "sara&miki", eventiOrganizzati, giudici);
        Organizzatore o2 = new Organizzatore("sara&miki", "miki&sara", eventiOrganizzati2, giudici);
        utentiRegistrati.add(o);
        utentiRegistrati.add(o2);

        Evento e = new Evento("Hackathon", "Faggiano",
                                LocalDate.of(2025, 6, 9), LocalDate.of(2025, 6, 11),
                                40, 3, LocalDate.of(2025, 6, 3), LocalDate.of(2025, 6, 7),
                                o, giudici, partecipanti );
        Evento e1 = new Evento("Hackaton-Speed", "Puccianiello",
                                LocalDate.of(2025, 11, 20), LocalDate.of(2025, 11, 22),
                                 30, 5, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 18),
                                o, giudici, partecipanti2);
        Evento e2 = new Evento("Hackaton-Go", "Roma",
                LocalDate.of(2025, 11, 20), LocalDate.of(2025, 11, 22),
                30, 5, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 18),
                o2, giudici,  partecipanti3);

        //se ci sono elementi non li rinserisce
        if(!eventiDisponibili.isEmpty()) {
            return;
        }

        eventiDisponibili.add(e);
        eventiDisponibili.add(e1);
        eventiDisponibili.add(e2);
        eventiOrganizzati.add(e);
        eventiOrganizzati.add(e1);
        eventiOrganizzati2.add(e2);
        e.setTeams(teams);
        e1.setTeams(teams1);
        e2.setTeams(teams2);

    }

    public static ArrayList<Evento> getEventiDisponibili() {
        return eventiDisponibili;
    }

   /* public static ArrayList<Team> getTeamDisponibili() {
        return teamDisponibili;
    }

    */

    public static ArrayList<Team> getTeamDisponibili(Evento evento){
        return evento.getTeams();
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
                utenteCorrente = u;
                return u;
            }
        }
        return null;
    }


    public static Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public static void setUtenteCorrente(Utente u) {
        utenteCorrente = u;
    }


    public static Partecipante getPartecipantCorrente() {
        return partecipantCorrente;
    }

    public static void setPartecipantCorrente(Partecipante p) {
        partecipantCorrente = p;
    }

    //Funzione che serve a controllare se vengono aggiunti effettivamente i partecipanti a quell evento
    public static void stampaPartecipantiEvento(Evento e) {
        System.out.println("Partecipanti evento: " + e.getTitolo());
        for(Partecipante p : e.getPartecipanti()) {
            System.out.println("-"+p.getLogin());
        }
    }
    public static boolean invitaGiudicePendente(Evento evento, Utente utente) {
        if (!(utente instanceof Giudice)&& !(utente instanceof Partecipante)){
            for(InvitoGiudice invito: invitiPendenti){
                if(invito.getEvento().equals(evento) && invito.getUtente().equals(utente.getLogin())) {
                    return false;
                }
            }
            invitiPendenti.add(new InvitoGiudice(evento, utente));
            return true;
        }
        return false;
    }

    public static boolean accettaInvitoGiudice(Evento evento, Utente utente) {
        InvitoGiudice invitoTrovato = null;

        for(InvitoGiudice invito: invitiPendenti){
            if(invito.getEvento().equals(evento) && invito.getUtente().equals(utente.getLogin()) && !invito.isAccettato()){
            invitoTrovato = invito;
            break;
            }
        }
        if(invitoTrovato != null) {
            invitoTrovato.accetta();
            Giudice nuovoGiudice = new Giudice(utente.getLogin(), utente.getPassword(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            evento.getGiudici().add(nuovoGiudice);
            invitiPendenti.remove(invitoTrovato);
            return true;
        }
        return false;
    }
}
