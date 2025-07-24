package controller;
import dao.*;

import model .*;
import javax.swing.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private UtenteDAO utenteDAO;
    private OrganizzatoreDAO organizzatoreDAO;
    private PartecipanteDAO partecipanteDAO;
    private EventoDAO eventoDAO;
    private TeamDAO teamDAO;
    private GiudiceDAO giudiceDAO;
    private InvitoGiudiceDAO invitoGiudiceDAO;
    private VotoDAO votoDAO;
    private DocumentoDAO documentoDAO;

    public Controller(UtenteDAO utenteDAO, OrganizzatoreDAO organizzatoreDAO, PartecipanteDAO partecipanteDAO, GiudiceDAO giudiceDAO, EventoDAO eventoDAO, TeamDAO teamDAO, InvitoGiudiceDAO invitoGiudiceDAO, DocumentoDAO documentoDAO, VotoDAO votoDAO) {
        this.utenteDAO = utenteDAO;
        this.organizzatoreDAO = organizzatoreDAO;
        this.partecipanteDAO = partecipanteDAO;
        this.eventoDAO = eventoDAO;
        this.teamDAO = teamDAO;
        this.giudiceDAO = giudiceDAO;
        this.invitoGiudiceDAO = invitoGiudiceDAO;
        this.documentoDAO = documentoDAO;
        this.votoDAO = votoDAO;
    }

    // Login utente (restituisce l'oggetto corretto)
    public Utente loginUtente(String login, String password) {
        Utente u = utenteDAO.getUtentebyLoginAndPassword(login, password);
        if (u == null){
            return null;
        }
        if (organizzatoreDAO.isOrganizzatore(u.getLogin())) {
            return organizzatoreDAO.getOrganizzatore(u.getLogin());
        }
        List<Evento> eventi = eventoDAO.getTuttiEventi();
        for (Evento evento : eventi) {
            Partecipante p = partecipanteDAO.getPartecipante(u.getLogin(), evento.getId());
            if (p != null) {
                return p;
            }
        }
        return u;
    }

    public boolean registraUtente (String email, String password) {
       Utente attuale = utenteDAO.getUtentebyLogin(email);
       if(attuale != null){
           return false;
       }
       Utente nuovoUtente = new Utente(email, password);
       return utenteDAO.addUtente(nuovoUtente);
    }

    public List<Utente> getUtentiRegistrati() {
        return utenteDAO.getAllUtenti();
    }

    public boolean eliminaUtente(String login) {
        return utenteDAO.deleteUtente(login);
    }

    public boolean aggiornaUtente(Utente utente) {
        return utenteDAO.updateUtente(utente);
    }

    public Utente getUtenteDaDB(String login) {
        return utenteDAO.getUtentebyLogin(login);
    }

    public boolean iscriviPartecipante (String login, int eventoId) {
        return partecipanteDAO.addPartecipante(login, eventoId);
    }

    public Partecipante getPartecipanteDaDB(String login, int eventoId) {
        return partecipanteDAO.getPartecipante(login, eventoId);
    }

    public Evento getEventoById(int id) {
        return eventoDAO.getEvento(id);
    }

    public Evento creaEvento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine, int nMaxIscritti, int dimMaxTeam, LocalDate inizioRegistrazioni, LocalDate fineRegistrazioni, Organizzatore organizzatore) {
        Evento nuovoEvento = new Evento (titolo, sede, dataInizio, dataFine, nMaxIscritti, dimMaxTeam, inizioRegistrazioni, fineRegistrazioni, organizzatore, new ArrayList<>(),  new ArrayList<>());
        // Salva nel DB e ottieni l'evento con id assegnato
        Evento eventoConId = eventoDAO.aggiungiEvento(nuovoEvento);
        if (eventoConId != null) {
            organizzatoreDAO.aggiungiOrganizzatore(organizzatore, eventoConId.getId());
            eventoConId.setDocumenti(new ArrayList<>());
            return eventoConId;
        }
        return null;
    }

    public List<Evento> getEventiOrganizzatore(String loginOrganizzatore) {
        return eventoDAO.getEventiPerOrganizzatore(loginOrganizzatore);
    }

    public List<Evento> getTuttiEventi() {
        return eventoDAO.getTuttiEventi();
    }

    public List<InvitoGiudice> getInvitiPendentiUtente(String login) {
        return invitoGiudiceDAO.getInvitiPendentiPerUtente(login);
    }
    public List <Utente> getUtentiInvitabili(int eventoId){
        ArrayList <Utente> invitabili = new ArrayList <>();
        for (Utente u : utenteDAO.getAllUtenti()) {
            if (organizzatoreDAO.isOrganizzatore(u.getLogin())) {
                continue;
            }
            boolean giaGiudiceInQuestoEvento = false;
            for (Giudice g: giudiceDAO.getGiudiciEvento(eventoId)) {
                if(g.getLogin().equals(u.getLogin())) {
                    giaGiudiceInQuestoEvento = true;
                    break;
                }
            }
            if(giaGiudiceInQuestoEvento){
                continue;
            }

            boolean giaPartecipanteInQuestoEvento = false;
            for (Partecipante p: partecipanteDAO.getPartecipantiEvento(eventoId)) {
                if(p.getLogin().equals(u.getLogin())) {
                    giaPartecipanteInQuestoEvento = true;
                    break;
                }
            }
            if(giaPartecipanteInQuestoEvento){
                continue;
            }

            if(invitoGiudiceDAO.esisteInvitoPendentePerUtenteEvento(u.getLogin(),eventoId)) {
                continue;
            }

            invitabili.add(u);
        }
        return invitabili;
    }

    public boolean accettaInvitoGiudice(int idInvito, String login) {
        // Imposta lo stato
        InvitoGiudice invito = invitoGiudiceDAO.getInvitoById(idInvito);
        int eventoId = invito.getEventoId();
        Partecipante partecipante = partecipanteDAO.getPartecipante(login, eventoId);
        if(partecipante != null){
            JOptionPane.showMessageDialog(null, "Non puoi accettare l'invito come giudice: sei già partecipante di questo evento", "error", JOptionPane.ERROR_MESSAGE);
            invito.setAccettato(false);
            invito.setRifiutato(true);
            invitoGiudiceDAO.updateInvitoGiudice(invito);
            return false;
        }
        invito.setAccettato(true);
        invito.setRifiutato(false);
        // Aggiorna nel database tramite DAO
        boolean invitoOK = invitoGiudiceDAO.updateInvitoGiudice(invito);
        if(invitoOK){
            return giudiceDAO.aggiungiGiudice(login, eventoId);
        }
        return false;
    }

    public boolean rifiutaInvitoGiudice(int invitoId, String login) {
        InvitoGiudice invito = invitoGiudiceDAO.getInvitoById(invitoId);
        invito.setAccettato(false);
        invito.setRifiutato(true);
        return invitoGiudiceDAO.updateInvitoGiudice(invito);
    }

    public boolean invitaGiudicePendente(int eventoId, String login) {
        if (giudiceDAO.getGiudice(login, eventoId) != null) {
            return false;
        }
        if(invitoGiudiceDAO.esisteInvitoPendentePerUtenteEvento(login, eventoId)){
            return false;
        }
        Evento evento = eventoDAO.getEvento(eventoId);
        Utente utente = utenteDAO.getUtentebyLogin(login);
        InvitoGiudice nuovoInvito = new InvitoGiudice(evento, utente, false, false);
        return invitoGiudiceDAO.addInvitoGiudice(nuovoInvito);
    }

    public Giudice getGiudiceEvento(String login, int eventoId) {
        return giudiceDAO.getGiudice(login, eventoId);
    }

    public List <Giudice> getGiudiciEvento(int eventoId) {
        return giudiceDAO.getGiudiciEvento(eventoId);
    }

    public List<Team> getTeamsEvento (int eventoId) {
        return teamDAO.getTeamEvento(eventoId);
    }

    // Crea un nuovo team e lo aggiunge al DB
    public Team creaTeam(String nomeTeam, String loginPartecipante, int eventoId) {
        Team team = new Team(nomeTeam, new ArrayList<>(), new ArrayList<>());
        teamDAO.aggiungiTeam(team, eventoId);
        // Unisci subito il partecipante al team appena creato
        partecipanteDAO.joinTeam(loginPartecipante, nomeTeam, eventoId);
        return teamDAO.getTeam(nomeTeam, eventoId);
    }

    public void unisciPartecipanteATeam (String loginPartecipante, String nomeTeam, int eventoId) {
        teamDAO.unisciPartecipanteATeam(loginPartecipante, nomeTeam, eventoId);
    }
    public boolean isPartecipanteInTeam(String loginPartecipante, String nomeTeam, int eventoId) {
        return teamDAO.isPartecipanteInTeam(loginPartecipante, nomeTeam, eventoId);
    }

    // Dimensione attuale del team
    public int getDimTeam(String nomeTeam, int eventoId) {
        return teamDAO.getDimTeam(nomeTeam, eventoId);
    }
    // Carica un documento nel DB
    public void caricaDocumento(Documento documento, String nomeTeam, int eventoId, String login) {
        documentoDAO.save(documento, nomeTeam, eventoId, login);
    }

    public List<Documento> getDocumentiEvento(int eventoId) {
        return documentoDAO.getDocumentiEvento(eventoId);
    }

    public List<Documento> getDocumentiTeamEvento(String nomeTeam, int eventoId) {
        return documentoDAO.getDocumentiTeamEvento(eventoId, nomeTeam);
    }

    public List<Documento> getDocumentiTeamEventoPartecipante(int eventoId, String nomeTeam, String login) {
        return documentoDAO.getDocumentiTeamEventoPartecipante(eventoId, nomeTeam, login);
    }

    public boolean teamHaDocumenti(String nomeTeam, int eventoId) {
        return documentoDAO.teamHaDocumenti(nomeTeam, eventoId);
    }

    public boolean giudiceHaVotatoTeam(String loginGiudice, String nomeTeam, int eventoId) {
        return votoDAO.giudiceHaVotatoTeam(loginGiudice, nomeTeam, eventoId);
    }

    public int getVotoDiGiudiceTeam(String loginGiudice, String nomeTeam, int eventoId) {
        return votoDAO.getVotoDiGiudiceTeam(loginGiudice, nomeTeam, eventoId);
    }

    public void votaTeam(String loginGiudice, String nomeTeam, int eventoId, int voto) {
        votoDAO.votaTeam(loginGiudice, nomeTeam, eventoId, voto);
    }

    public String getProblemaEvento(int eventoId) {
        return eventoDAO.getProblemaEvento(eventoId);
    }

    public void setProblemaEvento(int eventoId, String descrizione) {
        eventoDAO.setProblemaEvento(eventoId, descrizione);
    }

    public boolean assegnaGiudiceDescrizione (int eventoId, String loginGiudice) {
        if (giudiceDAO.getGiudice(loginGiudice, eventoId) != null) {
            return eventoDAO.setGiudiceDescrizione(eventoId, loginGiudice);
        }
        return false;
    }

    public Giudice getGiudiceDescrizione(int eventoId) {
        String login = eventoDAO.getLoginGiudiceDescrizione(eventoId); // recupera il login dal DB
        if (login != null) {
            return giudiceDAO.getGiudice(login, eventoId); // recupera l'oggetto Giudice dal DB
        }
        return null;
    }

    public boolean aggiungiCommentoGiudice(int documentoId, String utenteLogin, int eventoId, String commento) {
        return documentoDAO.aggiungiCommentoGiudice(documentoId, utenteLogin, eventoId, commento);
    }

    public List<CommentoGiudice> getCommentiDocumento(int idDocumento, int eventoId) {
        return documentoDAO.getCommentiDocumento(idDocumento, eventoId);
    }
}

