package controller;
import gui.Invito;
import model .*;
import javax.swing.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private ArrayList<Utente> utentiRegistrati;
    private ArrayList<Evento> eventiDisponibili;
    private Utente utenteCorrente = null;
    private Partecipante partecipantCorrente = null;
    private Organizzatore organizzatoreCorrente = null;
    private ArrayList<InvitoGiudice> invitiPendenti;
    private ArrayList<InvitoGiudice> invitiGiudice;

    public Controller() {
        this.utentiRegistrati = new ArrayList<>();
        this.eventiDisponibili = new ArrayList<>();
        this.invitiPendenti = new ArrayList<>();
        this.invitiGiudice = new ArrayList<>();
        initEventi();
    }

    public void initEventi() {
        Organizzatore o =new Organizzatore("miki&sara", "sara&miki", new ArrayList<>(), new ArrayList<>());
        Organizzatore o2 = new Organizzatore("sara&miki", "miki&sara", new ArrayList<>(), new ArrayList<>());

        aggiungiUtenteSeNuovo(o);
        aggiungiUtenteSeNuovo(o2);
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

   /* NON USATA
   public static ArrayList<Team> getTeamDisponibili(Evento evento){
        return evento.getTeams();
    }
    */

    public boolean registraUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email)) {
                return false; //utente già presente
            }
        }
        utentiRegistrati.add(new Utente(email, password));
        return true;
    }

    public boolean creaEvento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine, int nMaxIscritti, int dimMaxTeam, LocalDate inizioRegistrazioni, LocalDate fineRegistrazioni) {
        Evento nuovoEvento = new Evento (titolo, sede, dataInizio, dataFine, nMaxIscritti, dimMaxTeam, inizioRegistrazioni, fineRegistrazioni, organizzatoreCorrente, new ArrayList<>(),  new ArrayList<>());
        if(!eventiDisponibili.contains(nuovoEvento)) {
            eventiDisponibili.add(nuovoEvento);
            organizzatoreCorrente.getEventi().add(nuovoEvento);
            nuovoEvento.setDocumenti(new ArrayList<>());
            return true;
        }
        return false;
    }


    public Utente loginUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email) && u.getPassword().equals(password)) {
                if(u instanceof Organizzatore){
                    organizzatoreCorrente = (Organizzatore) u;
                    utenteCorrente = u;
                    return u;
                }

                for(Evento e: eventiDisponibili) {
                    for(Partecipante p: e.getPartecipanti()) {
                        if(p.getLogin().equals(u.getLogin())) {
                            utenteCorrente = p;
                            partecipantCorrente = p;
                            return p;
                        }
                    }
                }
                utenteCorrente = u;
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
        this.utenteCorrente = u;
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
        /*if((utente instanceof Partecipante)) {
            return false;
        }*/

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
        for (InvitoGiudice invito: invitiPendenti) {
            if(invito.getEvento().equals(evento)&&invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato()) {
                invitoTrovato = invito;
                break;
            }
        }
        if(invitoTrovato != null) {
            for(Partecipante p : evento.getPartecipanti()){
                if(p.getLogin().equals(utente.getLogin())) {
                    JOptionPane.showMessageDialog(null, "Non puoi accettare l'invito come giudice: sei già partecipante di questo evento", "Errore", JOptionPane.ERROR_MESSAGE);
                    invitiPendenti.remove(invitoTrovato);
                    return false;
                }
            }
            invitoTrovato.accetta();

            Giudice giudiceEsistente = null;

            for (Utente u : utentiRegistrati) {
                if (u instanceof Giudice && u.getLogin().equals(utente.getLogin())) {
                    giudiceEsistente = (Giudice) u;
                    break;
                }
            }

            if (giudiceEsistente != null) {
                if (!giudiceEsistente.getEventi().contains(evento)) {
                    giudiceEsistente.getEventi().add(evento);
                }
                evento.getGiudici().add(giudiceEsistente);
                if (utenteCorrente != null && utenteCorrente.getLogin().equals(utente.getLogin())) {
                    utenteCorrente = giudiceEsistente;
                }
                setUtenteCorrente(giudiceEsistente);
            }else{
                Giudice nuovoGiudice = new Giudice(utente.getLogin(), utente.getPassword(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                nuovoGiudice.getEventi().add(evento);
                utentiRegistrati.add(nuovoGiudice);
                evento.getGiudici().add(nuovoGiudice);
                if (utenteCorrente != null && utenteCorrente.getLogin().equals(utente.getLogin())) {
                    utenteCorrente = nuovoGiudice;
                }
                setUtenteCorrente(nuovoGiudice);
            }

            invitiPendenti.remove(invitoTrovato);
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
        System.out.println("Evento: " + evento.getTitolo());
        for (Giudice g : evento.getGiudici()) {
            System.out.println("Giudice presente: " + g.getLogin());
        }
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
            if(invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato() && !invito.isRifiutato()) {
                invitiUtente.add(invito.getEvento());
            }
        }
        return invitiUtente;
    }
    public void stampaUtentiRegistrati() {
        System.out.println("UTENTI REGISTRATI:");

        for (Utente u : utentiRegistrati) {
            String tipo = "Utente Generico";

            if (u instanceof Partecipante) {
                tipo = "Partecipante";
            } if (u instanceof Giudice) {
                tipo = "Giudice";
            }else if (u instanceof Organizzatore){ tipo = "Organizzatore";}

            System.out.println("- " + u.getLogin() + " (" + tipo + ")");
        }
    }

    public boolean assegnaGiudiceDescrizione (Evento evento, Giudice giudice) {
        if(evento.getGiudici().contains(giudice)){
            evento.setGiudiceDescrizione(giudice);
            return true;
        }
        return false;
    }

    public void caricaDocumento (Evento e, Documento documento) {
        e.getDocumenti().add(documento);
    }

    public ArrayList <Documento> getDocumentoTeam(Evento evento, Team team) {
        ArrayList <Documento> documento = new ArrayList <>();
        for(Documento doc: evento.getDocumenti()){
            if(doc.getTeam().equals(team)){
                documento.add(doc);
            }
        }
        return documento;
    }
}
