package dao;

import model.Evento;
import java.util.List;

/**
 * Interfaccia DAO per la gestione degli eventi.
 * Definisce i metodi per il recupero, aggiunta, aggiornamento, eliminazione e gestione delle proprietà degli eventi,
 * inclusa la relazione con organizzatori, giudici responsabili della descrizione e problema proposto.
 */
public interface EventoDAO {
    /**
     * Restituisce la lista degli eventi organizzati da uno specifico organizzatore.
     * @param login login dell'organizzatore
     * @return lista degli eventi dell'organizzatore
     */
    List<Evento> getEventiPerOrganizzatore(String login);

    /**
     * Restituisce l'evento attivo, cioè quello in cui le registrazioni sono aperte.
     * @return evento attivo o null se non esiste
     */
    Evento getEventoAttivo();

    /**
     * Restituisce l'evento corrispondente all'id fornito.
     * @param eventoId identificativo dell'evento
     * @return evento trovato o null se non esiste
     */
    Evento getEvento(int eventoId);

    /**
     * Restituisce la lista di tutti gli eventi presenti nel database.
     * @return lista di eventi
     */
    List<Evento> getTuttiEventi();

    /**
     * Aggiunge un nuovo evento al database.
     * @param evento evento da aggiungere
     * @return evento aggiunto con id valorizzato, o null se errore
     */
    Evento aggiungiEvento(Evento evento);

    /**
     * Aggiorna i dati di un evento esistente.
     * @param evento evento con i dati aggiornati
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean aggiornaEvento(Evento evento);

    /**
     * Elimina un evento dal database.
     * @param eventoId id dell'evento da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
    boolean eliminaEvento(int eventoId);

    /**
     * Imposta il DAO per la gestione degli organizzatori.
     * @param organizzatoreDAO implementazione del DAO organizzatore
     */
    void setOrganizzatoreDAO (OrganizzatoreDAO organizzatoreDAO);

    /**
     * Restituisce la descrizione del problema proposto per uno specifico evento.
     * @param eventoId id dell'evento
     * @return descrizione del problema
     */
    String getProblemaEvento(int eventoId);

    /**
     * Aggiorna la descrizione del problema per uno specifico evento.
     * @param eventoId id dell'evento
     * @param descrizione nuova descrizione del problema
     */
    void setProblemaEvento(int eventoId, String descrizione);

    /**
     * Restituisce il login del giudice responsabile della descrizione del problema per un evento.
     * @param eventoId id dell'evento
     * @return login del giudice responsabile della descrizione
     */
    String getLoginGiudiceDescrizione (int eventoId);

    /**
     * Aggiorna il giudice responsabile della descrizione del problema per un evento.
     * @param eventoId id dell'evento
     * @param loginGiudice login del giudice da impostare
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean setGiudiceDescrizione (int eventoId, String loginGiudice);
}
