package dao;

import model.Partecipante;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei partecipanti.
 * Definisce i metodi per il recupero, aggiunta, aggiornamento, eliminazione e gestione dei partecipanti associati agli eventi e ai team.
 */
public interface PartecipanteDAO {

    /**
     * Aggiunge un partecipante ad un evento.
     * @param login login del partecipante da aggiungere
     * @param idEvento identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    boolean addPartecipante (String login, int idEvento);

    /**
     * Restituisce il partecipante associato al login e all'evento specificato.
     * @param login login del partecipante
     * @param eventoId identificativo dell'evento
     * @return Partecipante trovato oppure null se non esiste
     */
    Partecipante getPartecipante(String login, int eventoId);

    /**
     * Restituisce la lista dei partecipanti ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista dei partecipanti dell'evento
     */
    List<Partecipante> getPartecipantiEvento(int eventoId);

    /**
     * Restituisce la lista dei partecipanti di un team per un evento specifico.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return lista dei partecipanti del team
     */
    List<Partecipante> getPartecipantiTeam(String nomeTeam, int eventoId);

    /**
     * Aggiunge un partecipante (oggetto) ad un evento specifico, assegnandolo eventualmente ad un team.
     * @param p partecipante da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    boolean aggiungiPartecipante(Partecipante p, int eventoId);

    /**
     * Aggiorna i dati di un partecipante per un evento.
     * @param p partecipante da aggiornare
     * @param eventoId identificativo dell'evento
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    boolean aggiornaPartecipante(Partecipante p, int eventoId);

    /**
     * Elimina un partecipante da un evento.
     * @param login login del partecipante da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean eliminaPartecipante(String login, int eventoId);

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO implementazione del DAO utente
     */
    void setUtenteDAO (UtenteDAO utenteDAO);

    /**
     * Assegna un partecipante ad un team per l'evento specificato.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     */
    void joinTeam(String loginPartecipante, String nomeTeam, int eventoId);
}
