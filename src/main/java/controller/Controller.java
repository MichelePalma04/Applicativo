package controller;
import gui.Invito;
import model .*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private ArrayList<Utente> utentiRegistrati;
    private ArrayList<Evento> eventiDisponibili;
    private Utente utenteCorrente = null;
    private Partecipante partecipantCorrente = null;
    private ArrayList<InvitoGiudice> invitiPendenti;
    private ArrayList<InvitoGiudice> invitiGiudice;
    //private static final ArrayList <Team> teamDisponibili = new ArrayList();

    public Controller() {
        this.utentiRegistrati = new ArrayList<>();
        this.eventiDisponibili = new ArrayList<>();
        this.invitiPendenti = new ArrayList<>();
        this.invitiGiudice = new ArrayList<>();
        initEventi();
    }


    public void initEventi() {
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
        teams.add(new Team ("Team 1",partecipante1Team1, null));
        teams.add(new Team ("Team 2",partecipante2Team1, null));
        teams.add(new Team ("Team 3",partecipante3Team1, null));

        ArrayList <Team> teams1 = new ArrayList();
        teams1.add(new Team ("Team A",partecipante1Team2, null));
        teams1.add(new Team ("Team B",partecipante2Team2, null));
        teams1.add(new Team ("Team C",partecipante3Team2, null));

        ArrayList <Team> teams2 = new ArrayList();
        teams2.add(new Team ("Team X",partecipante1Team3, null));
        teams2.add(new Team ("Team Y",partecipante2Team3, null));
        teams2.add(new Team ("Team Z",partecipante3Team3, null));

        //Organizzatori gia presenti in piattaforma
        Organizzatore o =new Organizzatore("miki&sara", "sara&miki", eventiOrganizzati, giudici);
        Organizzatore o2 = new Organizzatore("sara&miki", "miki&sara", eventiOrganizzati2, giudici);
        aggiungiUtenteSeNuovo(o);
        aggiungiUtenteSeNuovo(o2);

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
        /*if(!eventiDisponibili.isEmpty()) {
            return;
        }
         */

        aggiungiEventoSeNuovo(e);
        aggiungiEventoSeNuovo(e1);
        aggiungiEventoSeNuovo(e2);

        //eventiDisponibili.add(e);
        //eventiDisponibili.add(e1);
        //eventiDisponibili.add(e2);
        eventiOrganizzati.add(e);
        eventiOrganizzati.add(e1);
        eventiOrganizzati2.add(e2);
        e.setTeams(teams);
        e1.setTeams(teams1);
        e2.setTeams(teams2);

    }

    public void aggiungiUtenteSeNuovo(Utente utente){
        for(Utente u: utentiRegistrati){
            if(u.getLogin().equals(utente.getLogin())){
                return;
            }
        }
        utentiRegistrati.add(utente);
    }

    public void aggiungiEventoSeNuovo(Evento evento){
        for(Evento e: eventiDisponibili){
            //supponiamo che non ci siano eventi con lo stesso Titolo svolti nello stesso periodo
            if(e.getTitolo().equals(evento.getTitolo()) && e.getDataInizio().equals(evento.getDataInizio()) && e.getDataFine().equals(evento.getDataFine())){
                return;
            }
        }
        eventiDisponibili.add(evento);
    }

    public ArrayList<Evento> getEventiDisponibili() {
        return eventiDisponibili;
    }


    public static ArrayList<Team> getTeamDisponibili(Evento evento){
        return evento.getTeams();
    }

    public boolean registraUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email)) {
                return false; //utente già presente
            }
        }
        utentiRegistrati.add(new Utente(email, password));
        return true;
    }

    public Utente loginUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email) && u.getPassword().equals(password)) {
                utenteCorrente = u;
                if (u instanceof Partecipante) {
                    partecipantCorrente=(Partecipante)u;
                }else{
                    for(Evento e:eventiDisponibili) {
                        for(Partecipante p:e.getPartecipanti()) {
                            if(p.getLogin().equals(u.getLogin())) {
                                partecipantCorrente=p;
                                break;
                            }
                        }
                    }
                }
                return u;
            }
        }
        return null;
    }


    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public ArrayList<Utente> getUtentiRegistrati() {
        return utentiRegistrati;
    }

    public void setUtenteCorrente(Utente u) {
        utenteCorrente = u;
    }


    public Partecipante getPartecipantCorrente() {
        return partecipantCorrente;
    }

    public void setPartecipantCorrente(Partecipante p) {
        partecipantCorrente = p;
    }

    public Giudice getGiudiceCorrente(Evento evento) {
        for (Giudice giudice: evento.getGiudici()) {
            if(giudice.getLogin().equals(utenteCorrente.getLogin())) {
                return giudice;
            }
        }
        return null;
    }

    //Funzione che serve a controllare se vengono aggiunti effettivamente i partecipanti a quell evento
    public void stampaPartecipantiEvento(Evento e) {
        System.out.println("Partecipanti evento: " + e.getTitolo());
        for(Partecipante p : e.getPartecipanti()) {
            System.out.println("-"+p.getLogin());
        }
    }

    public boolean invitaGiudicePendente(Evento evento, Utente utente) {
        if((utente instanceof Partecipante)) {
            return false;
        }

        for(Giudice giudice: evento.getGiudici()) {
            if(giudice.getLogin().equals(utente.getLogin())) {
                return false;
            }
        }
        for(InvitoGiudice invito: invitiPendenti){
            if(invito.getEvento().equals(evento)&&invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato() && !invito.isRifiutato()) {
                return false;
            }
        }
        invitiPendenti.add(new InvitoGiudice(evento, utente));
        return true;
    }

    public boolean accettaInvitoGiudice(Evento evento, Utente utente) {
        InvitoGiudice invitoTrovato = null;

        for(InvitoGiudice invito: invitiPendenti){
            if(invito.getEvento().equals(evento) && invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato()){
            invitoTrovato = invito;
            break;
            }
        }
        if(invitoTrovato != null) {
            invitoTrovato.accetta();
            Giudice nuovoGiudice = new Giudice(utente.getLogin(), utente.getPassword(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            utentiRegistrati.remove(utente);
            utentiRegistrati.add(nuovoGiudice);
            evento.getGiudici().add(nuovoGiudice);
            if(utenteCorrente!= null & utenteCorrente .getLogin().equals(utente.getLogin())) {
                utenteCorrente=nuovoGiudice;
                partecipantCorrente=null;
            }
           // utentiRegistrati.add(nuovoGiudice);
            invitiPendenti.remove(invitoTrovato);
            setUtenteCorrente(nuovoGiudice);
            setPartecipantCorrente(null);
            System.out.println("Giudici dell'evento "+ evento.getTitolo() + " dopo l'accettazione.");
            for (Giudice g : evento.getGiudici()) {
                System.out.println("- " + g.getLogin());
            }
            stampaUtentiRegistrati();


            return true;
        }
        return false;
    }

    public boolean rifiutaInvitoGiudice (Evento evento, Utente utente) {
        InvitoGiudice invitoDaRimuovere = null;

        //uso di new ArrayList <> così da evitare problemi nella rimozioni di elementi durante il for
        for (InvitoGiudice invito : new ArrayList <> (invitiPendenti)) {
            if(invito.getEvento().equals(evento) && invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato()){
                invito.setRifiutato();
                invitiPendenti.remove(invito);
                return true;
            }
        }
        return false;
    }

    public boolean isUtenteGiudice(Evento evento, Utente utente) {
        for (Giudice g : evento.getGiudici()) {
            if (g.getLogin().equals(utente.getLogin())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList <Utente> getUtentiInvitabili(Evento evento){
        ArrayList <Utente> invitabili = new ArrayList <>();
        for (Utente u : utentiRegistrati) {
            if (u instanceof Organizzatore) {
                continue;
            }
            boolean giaGiudiceInQuestoEvento = false;
            boolean giaPartecipanteInQuestoEvento = false;
            boolean giaInvitatoAQuestoEvento = false;

            for (Giudice g: evento.getGiudici()) {
                if(g.getLogin().equals(u.getLogin())) {
                    giaGiudiceInQuestoEvento = true;
                    break;
                }
            }

            for (Partecipante p: evento.getPartecipanti()) {
                if(p.getLogin().equals(u.getLogin())) {
                    giaPartecipanteInQuestoEvento = true;
                    break;
                }
            }

            for (InvitoGiudice invito: invitiPendenti) {
                if(invito.getEvento().equals(evento) && invito.getUtente().getLogin().equals(u.getLogin()) && !invito.isAccettato() && !invito.isRifiutato()) {
                    giaInvitatoAQuestoEvento = true;
                    break;
                }
            }

            if(!giaGiudiceInQuestoEvento && !giaPartecipanteInQuestoEvento && !giaInvitatoAQuestoEvento){
                invitabili.add(u);
            }
        }
        return invitabili;
    }



    public void aggiungiInvitoGiudice (InvitoGiudice invito){
        invitiGiudice.add(invito);
    }

    public ArrayList <Evento> getInvitiUtente(Utente utente) {
        ArrayList <Evento> invitiUtente = new ArrayList <>();
        for(InvitoGiudice invito: invitiPendenti){
            if(invito.getUtente().equals(utente) && !invito.isAccettato() && !invito.isRifiutato()) {
                invitiUtente.add(invito.getEvento());
            }
        }
        return invitiUtente;
    }
    public void stampaUtentiRegistrati() {
        System.out.println("UTENTI REGISTRATI:");
        for (Utente u : utentiRegistrati) {
            String tipo = "Utente Generico";
            if (u instanceof Partecipante) tipo = "Partecipante";
            else if (u instanceof Giudice) tipo = "Giudice";
            else if (u instanceof Organizzatore) tipo = "Organizzatore";

            System.out.println("- " + u.getLogin() + " (" + tipo + ")");
        }
    }
}
