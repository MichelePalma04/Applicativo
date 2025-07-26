package dao;


import model.InvitoGiudice;

import java.util.List;

/**
 * Interfaccia DAO per la gestione degli inviti ai giudici.
 * Definisce i metodi per la creazione, aggiornamento, cancellazione e recupero degli inviti associati agli eventi e agli utenti giudici.
 */
public interface InvitoGiudiceDAO {
    /**
     * Aggiunge un nuovo invito giudice al database.
     * @param invito invito da aggiungere
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    boolean addInvitoGiudice(InvitoGiudice invito);

    /**
     * Aggiorna lo stato di un invito giudice (accettato/rifiutato).
     * @param invito invito da aggiornare
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean updateInvitoGiudice(InvitoGiudice invito);

    /**
     * Verifica se esiste un invito pendente per uno specifico utente e evento.
     * @param login login dell'utente
     * @param eventoId identificativo dell'evento
     * @return true se esiste almeno un invito pendente, false altrimenti
     */
    boolean esisteInvitoPendentePerUtenteEvento(String login, int eventoId);

    /**
     * Elimina un invito giudice dal database tramite identificativo.
     * @param idInvito identificativo dell'invito da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
    boolean deleteInvitoGiudice(int idInvito);

    /**
     * Restituisce l'invito tramite identificativo.
     * @param idInvito identificativo dell'invito
     * @return invito trovato oppure null se non esiste
     */
    InvitoGiudice getInvitoById(int idInvito);

    /**
     * Restituisce la lista degli inviti pendenti per uno specifico utente.
     * @param loginUtente login dell'utente
     * @return lista degli inviti pendenti
     */
    List<InvitoGiudice> getInvitiPendentiPerUtente(String loginUtente);

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO istanza di UtenteDAO
     */
    void setUtenteDAO(UtenteDAO utenteDAO);

    /**
     * Imposta il DAO per la gestione degli eventi.
     * @param eventoDAO istanza di EventoDAO
     */
    void setEventoDAO(EventoDAO eventoDAO);
}
