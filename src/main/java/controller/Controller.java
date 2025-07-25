package controller;
import dao.*;

import model .*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller principale dell'applicazione.
 * Gestisce la logica tra la GUI e i DAO, coordinando operazioni su Utenti, Eventi, Team, Giudici, Documenti, Voti, Partecipanti e Inviti.
 */
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

    /**
     * Costruttore che inizializza tutte le dipendenze necessarie tramite i DAO.
     * @param utenteDAO implementazione per la gestione degli utenti
     * @param organizzatoreDAO implementazione per la gestione degli organizzatori
     * @param partecipanteDAO implementazione per la gestione dei partecipanti
     * @param giudiceDAO implementazione per la gestione dei giudici
     * @param eventoDAO implementazione per la gestione degli eventi
     * @param teamDAO implementazione per la gestione dei team
     * @param invitoGiudiceDAO implementazione per la gestione degli inviti ai giudici
     * @param documentoDAO implementazione per la gestione dei documenti
     * @param votoDAO implementazione per la gestione dei voti
     */

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

    /**
     * Effettua il login di un utente.
     * Restituisce l'oggetto corrispondente: Organizzatore, Partecipante o Utente.
     * @param login login dell'utente
     * @param password password dell'utente
     * @return l'oggetto corrispondente al ruolo dell'utente, oppure null se non trovato
     */
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

    /**
     * Registra un nuovo utente se non già presente.
     * @param login login dell'utente
     * @param password password dell'utente
     * @return true se la registrazione ha successo, false altrimenti
     */
    public boolean registraUtente (String login, String password) {
       Utente attuale = utenteDAO.getUtentebyLogin(login);
       if(attuale != null){
           return false;
       }
       Utente nuovoUtente = new Utente(login, password);
       return utenteDAO.addUtente(nuovoUtente);
    }

    /**
     * Restituisce la lista di tutti gli utenti registrati.
     * @return lista di utenti
     */
    public List<Utente> getUtentiRegistrati() {
        return utenteDAO.getAllUtenti();
    }

    /**
     * Elimina un utente dal database.
     * @param login login dell'utente da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    public boolean eliminaUtente(String login) {
        return utenteDAO.deleteUtente(login);
    }

    /**
     * Aggiorna i dati di un utente.
     * @param utente utente aggiornato
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    public boolean aggiornaUtente(Utente utente) {
        return utenteDAO.updateUtente(utente);
    }

    /**
     * Restituisce l'utente dal database tramite login.
     * @param login login dell'utente
     * @return oggetto Utente trovato oppure null
     */
    public Utente getUtenteDaDB(String login) {
        return utenteDAO.getUtentebyLogin(login);
    }

    /**
     * Iscrive un partecipante ad un evento.
     * @param login login del partecipante
     * @param eventoId identificativo dell'evento
     * @return true se l'iscrizione ha successo, false altrimenti
     */
    public boolean iscriviPartecipante (String login, int eventoId) {
        return partecipanteDAO.addPartecipante(login, eventoId);
    }

    /**
     * Restituisce il partecipante tramite login ed evento.
     * @param login login del partecipante
     * @param eventoId identificativo dell'evento
     * @return oggetto Partecipante trovato oppure null
     */
    public Partecipante getPartecipanteDaDB(String login, int eventoId) {
        return partecipanteDAO.getPartecipante(login, eventoId);
    }

    /**
     * Restituisce l'evento tramite ID.
     * @param id identificativo dell'evento
     * @return oggetto Evento trovato oppure null
     */
    public Evento getEventoById(int id) {
        return eventoDAO.getEvento(id);
    }

    /**
     * Crea un nuovo evento e lo aggiunge al database dopo validazione delle date.
     * @param nuovoEvento evento da aggiungere
     * @return evento creato con id valorizzato oppure null se errore
     */
    public Evento creaEvento(Evento nuovoEvento) {
        try {
            nuovoEvento.validaDate();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Errore date evento", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        Evento eventoConId = eventoDAO.aggiungiEvento(nuovoEvento);
        if (eventoConId != null) {
            organizzatoreDAO.aggiungiOrganizzatore(nuovoEvento.getOrganizzatore(), eventoConId.getId());
            eventoConId.setDocumenti(new ArrayList<>());
            return eventoConId;
        }
        return null;
    }

    /**
     * Restituisce la lista degli eventi creati da un organizzatore.
     * @param loginOrganizzatore login dell'organizzatore
     * @return lista di eventi
     */
    public List<Evento> getEventiOrganizzatore(String loginOrganizzatore) {
        return eventoDAO.getEventiPerOrganizzatore(loginOrganizzatore);
    }

    /**
     * Restituisce la lista di tutti gli eventi.
     * @return lista di eventi
     */
    public List<Evento> getTuttiEventi() {
        return eventoDAO.getTuttiEventi();
    }

    /**
     * Restituisce la lista degli inviti giudice pendenti per un utente.
     * @param login login dell'utente
     * @return lista di inviti pendenti
     */
    public List<InvitoGiudice> getInvitiPendentiUtente(String login) {
        return invitoGiudiceDAO.getInvitiPendentiPerUtente(login);
    }

    /**
     * Restituisce la lista degli utenti invitabili come giudici per un evento.
     * @param eventoId identificativo dell'evento
     * @return lista di utenti invitabili
     */
    public List<Utente> getUtentiInvitabili(int eventoId) {
        List<Giudice> giudiciEvento = giudiceDAO.getGiudiciEvento(eventoId);
        List<Partecipante> partecipantiEvento = partecipanteDAO.getPartecipantiEvento(eventoId);

        List<Utente> invitabili = new ArrayList<>();
        for (Utente u : utenteDAO.getAllUtenti()) {
            boolean invita = !organizzatoreDAO.isOrganizzatore(u.getLogin()) && !isGiudiceInEvento(u, giudiciEvento) && !isPartecipanteInEvento(u, partecipantiEvento) && !invitoGiudiceDAO.esisteInvitoPendentePerUtenteEvento(u.getLogin(), eventoId);

            if (invita) {
                invitabili.add(u);
            }
        }
        return invitabili;
    }

    /**
     * Verifica se l'utente è giudice nell'evento.
     * @param u utente da verificare
     * @param giudiciEvento lista dei giudici dell'evento
     * @return true se l'utete è presente tra i giudici dell'evento, false altrimenti
     */
    private boolean isGiudiceInEvento(Utente u, List<Giudice> giudiciEvento) {
        for (Giudice g : giudiciEvento) {
            if (g.getLogin().equals(u.getLogin())) return true;
        }
        return false;
    }

    /**
     * Verifica se l'utente è partecipante nell'evento.
     * @param u utente da verificare
     * @param partecipantiEvento lista dei partecipanti dell'evento
     * @return true se l'utente è tra i partecipanti dell'evento, false altrimenti
     */
    private boolean isPartecipanteInEvento(Utente u, List<Partecipante> partecipantiEvento) {
        for (Partecipante p : partecipantiEvento) {
            if (p.getLogin().equals(u.getLogin())) return true;
        }
        return false;
    }

    /**
     * Accetta un invito giudice per un utente. Aggiorna stato invito e aggiunge giudice.
     * @param idInvito id dell'invito
     * @param login login dell'utente che accetta
     * @return true se accettazione e aggiunta giudice hanno successo, false altrimenti
     */
    public boolean accettaInvitoGiudice(int idInvito, String login) {
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

    /**
     * Rifiuta un invito giudice per un utente. Aggiorna stato invito.
     * @param invitoId id dell'invito
     * @return true se aggiornamento ha successo, false altrimenti
     */
    public boolean rifiutaInvitoGiudice(int invitoId) {
        InvitoGiudice invito = invitoGiudiceDAO.getInvitoById(invitoId);
        invito.setAccettato(false);
        invito.setRifiutato(true);
        return invitoGiudiceDAO.updateInvitoGiudice(invito);
    }

    /**
     * Invia un invito giudice pendente a un utente per un evento.
     * @param eventoId id dell'evento
     * @param login login dell'utente da invitare
     * @return true se invio invito ha successo, false altrimenti
     */
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

    /**
     * Restituisce il giudice associato ad un evento.
     * @param login login del giudice
     * @param eventoId id dell'evento
     * @return oggetto Giudice oppure null
     */
    public Giudice getGiudiceEvento(String login, int eventoId) {
        return giudiceDAO.getGiudice(login, eventoId);
    }

    /**
     * Restituisce la lista dei giudici associati ad un evento.
     * @param eventoId id dell'evento
     * @return lista di giudici
     */
    public List <Giudice> getGiudiciEvento(int eventoId) {
        return giudiceDAO.getGiudiciEvento(eventoId);
    }

    /**
     * Restituisce la lista dei team associati ad un evento.
     * @param eventoId id dell'evento
     * @return lista di team
     */
    public List<Team> getTeamsEvento (int eventoId) {
        return teamDAO.getTeamEvento(eventoId);
    }

    /**
     * Crea un nuovo team e aggiunge il partecipante creatore.
     * @param nomeTeam nome del team
     * @param loginPartecipante login del partecipante
     * @param eventoId id dell'evento
     * @return oggetto Team creato
     */
    public Team creaTeam(String nomeTeam, String loginPartecipante, int eventoId) {
        Team team = new Team(nomeTeam, new ArrayList<>(), new ArrayList<>());
        teamDAO.aggiungiTeam(team, eventoId);
        // Unisci subito il partecipante al team appena creato
        partecipanteDAO.joinTeam(loginPartecipante, nomeTeam, eventoId);
        return teamDAO.getTeam(nomeTeam, eventoId);
    }

    /**
     * Unisce un partecipante a un team.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     */
    public void unisciPartecipanteATeam (String loginPartecipante, String nomeTeam, int eventoId) {
        teamDAO.unisciPartecipanteATeam(loginPartecipante, nomeTeam, eventoId);
    }

    /**
     * Verifica se un partecipante è in un team.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @return true se il partecipante è nel team, false altrimenti
     */
    public boolean isPartecipanteInTeam(String loginPartecipante, String nomeTeam, int eventoId) {
        return teamDAO.isPartecipanteInTeam(loginPartecipante, nomeTeam, eventoId);
    }

    /**
     * Restituisce la dimensione attuale di un team.
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @return numero di partecipanti nel team
     */
    public int getDimTeam(String nomeTeam, int eventoId) {
        return teamDAO.getDimTeam(nomeTeam, eventoId);
    }

    /**
     * Carica un documento associato a un team e a un evento.
     * @param documento documento da caricare
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @param login login del partecipante
     */
    public void caricaDocumento(Documento documento, String nomeTeam, int eventoId, String login) {
        documentoDAO.save(documento, nomeTeam, eventoId, login);
    }

    /**
     * Restituisce la lista dei documenti associati a un evento.
     * @param eventoId id dell'evento
     * @return lista di documenti
     */
    public List<Documento> getDocumentiEvento(int eventoId) {
        return documentoDAO.getDocumentiEvento(eventoId);
    }

    /**
     * Restituisce la lista dei documenti associati a un team e a un evento.
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @return lista di documenti
     */
    public List<Documento> getDocumentiTeamEvento(String nomeTeam, int eventoId) {
        return documentoDAO.getDocumentiTeamEvento(eventoId, nomeTeam);
    }

    /**
     * Restituisce la lista dei documenti associati a un team, evento e partecipante.
     * @param eventoId id dell'evento
     * @param nomeTeam nome del team
     * @param login login del partecipante
     * @return lista di documenti
     */
    public List<Documento> getDocumentiTeamEventoPartecipante(int eventoId, String nomeTeam, String login) {
        return documentoDAO.getDocumentiTeamEventoPartecipante(eventoId, nomeTeam, login);
    }

    /**
     * Verifica se un team ha documenti per un evento.
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @return true se il team ha documenti, false altrimenti
     */
    public boolean teamHaDocumenti(String nomeTeam, int eventoId) {
        return documentoDAO.teamHaDocumenti(nomeTeam, eventoId);
    }

    /**
     * Verifica se un giudice ha già votato un team per un evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @return true se il giudice ha votato, false altrimenti
     */
    public boolean giudiceHaVotatoTeam(String loginGiudice, String nomeTeam, int eventoId) {
        return votoDAO.giudiceHaVotatoTeam(loginGiudice, nomeTeam, eventoId);
    }

    /**
     * Restituisce il voto assegnato da un giudice a un team per un evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @return valore del voto, -1 se non trovato
     */
    public int getVotoDiGiudiceTeam(String loginGiudice, String nomeTeam, int eventoId) {
        return votoDAO.getVotoDiGiudiceTeam(loginGiudice, nomeTeam, eventoId);
    }

    /**
     * Registra il voto di un giudice per un team in un evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId id dell'evento
     * @param voto valore del voto
     */
    public void votaTeam(String loginGiudice, String nomeTeam, int eventoId, int voto) {
        votoDAO.votaTeam(loginGiudice, nomeTeam, eventoId, voto);
    }

    /**
     * Restituisce la descrizione del problema associato ad un evento.
     * @param eventoId id dell'evento
     * @return descrizione del problema
     */
    public String getProblemaEvento(int eventoId) {
        return eventoDAO.getProblemaEvento(eventoId);
    }

    /**
     * Aggiorna la descrizione del problema associato ad un evento.
     * @param eventoId id dell'evento
     * @param descrizione nuova descrizione del problema
     */
    public void setProblemaEvento(int eventoId, String descrizione) {
        eventoDAO.setProblemaEvento(eventoId, descrizione);
    }

    /**
     * Assegna un giudice come responsabile della descrizione del problema per un evento.
     * @param eventoId id dell'evento
     * @param loginGiudice login del giudice
     * @return true se assegnazione ha successo, false altrimenti
     */
    public boolean assegnaGiudiceDescrizione (int eventoId, String loginGiudice) {
        if (giudiceDAO.getGiudice(loginGiudice, eventoId) != null) {
            return eventoDAO.setGiudiceDescrizione(eventoId, loginGiudice);
        }
        return false;
    }

    /**
     * Restituisce il giudice responsabile della descrizione del problema per un evento.
     * @param eventoId id dell'evento
     * @return oggetto Giudice oppure null
     */
    public Giudice getGiudiceDescrizione(int eventoId) {
        String login = eventoDAO.getLoginGiudiceDescrizione(eventoId);
        if (login != null) {
            return giudiceDAO.getGiudice(login, eventoId);
        }
        return null;
    }

    /**
     * Aggiunge un commento di un giudice ad un documento associato ad un evento.
     * @param documentoId id del documento
     * @param utenteLogin login del giudice
     * @param eventoId id dell'evento
     * @param commento testo del commento
     * @return true se inserimento ha successo, false altrimenti
     */
    public boolean aggiungiCommentoGiudice(int documentoId, String utenteLogin, int eventoId, String commento) {
        return documentoDAO.aggiungiCommentoGiudice(documentoId, utenteLogin, eventoId, commento);
    }

    /**
     * Restituisce la lista dei commenti dei giudici per un documento e un evento.
     * @param idDocumento id del documento
     * @param eventoId id dell'evento
     * @return lista di commenti dei giudici
     */
    public List<CommentoGiudice> getCommentiDocumento(int idDocumento, int eventoId) {
        return documentoDAO.getCommentiDocumento(idDocumento, eventoId);
    }
}

