package dao;

import model.Voto;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei voti.
 * Definisce i metodi per il recupero, aggiunta, aggiornamento, eliminazione e gestione dei voti assegnati dai giudici ai team per gli eventi.
 */
public interface VotoDAO {
    /**
     * Restituisce il voto tramite identificativo.
     * @param id identificativo del voto
     * @return Voto trovato oppure null se non esiste
     */
    Voto getVoto(int id);

    /**
     * Restituisce la lista dei voti assegnati ad un team per uno specifico evento.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return lista dei voti del team
     */
    List<Voto> getVotiTeam(String nomeTeam, int eventoId);

    /**
     * Restituisce la lista dei voti assegnati da un giudice.
     * @param giudiceLogin login del giudice
     * @return lista dei voti assegnati dal giudice
     */
    List<Voto> getVotiGiudice(String giudiceLogin);

    /**
     * Aggiorna il valore di un voto.
     * @param voto voto aggiornato
     * @param id identificativo del voto da aggiornare
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    boolean aggiornaVoto(Voto voto, int id);

    /**
     * Elimina un voto dal database tramite identificativo.
     * @param id identificativo del voto da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean eliminaVoto(int id);


    /**
     * Imposta il DAO per la gestione dei giudici.
     * @param giudiceDAO istanza di GiudiceDAO
     */
    void setGiudiceDAO (GiudiceDAO giudiceDAO);

    /**
     * Verifica se un giudice ha già votato un team per uno specifico evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return true se il giudice ha già votato il team, false altrimenti
     */
    boolean giudiceHaVotatoTeam (String loginGiudice, String nomeTeam, int eventoId);

    /**
     * Registra un voto per un team da parte di un giudice in un evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @param voto valore del voto da assegnare
     */
    void votaTeam (String loginGiudice, String nomeTeam, int eventoId, int voto);

    /**
     * Restituisce il valore del voto assegnato da un giudice ad un team per uno specifico evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return valore del voto, -1 se non trovato
     */
    int getVotoDiGiudiceTeam (String loginGiudice, String nomeTeam, int eventoId);
}
