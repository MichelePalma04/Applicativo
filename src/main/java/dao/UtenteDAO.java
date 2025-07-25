package dao;
import model.Utente;

import java.util.List;

/**
 * Interfaccia DAO per la gestione degli utenti.
 * Definisce i metodi per il recupero, aggiunta, aggiornamento ed eliminazione degli utenti.
 */
public interface UtenteDAO {

    /**
     * Restituisce l'utente associato al login specificato.
     * @param login login dell'utente
     * @return Utente trovato oppure null se non esiste
     */
    Utente getUtentebyLogin(String login);

    /**
     * Restituisce l'utente associato a login e password specificati.
     * @param login login dell'utente
     * @param password password dell'utente
     * @return Utente trovato oppure null se non esiste
     */
    Utente getUtentebyLoginAndPassword(String login, String password);

    /**
     * Restituisce la lista di tutti gli utenti presenti nel database.
     * @return lista di utenti
     */
    List<Utente> getAllUtenti();

    /**
     * Aggiunge un nuovo utente al database.
     * @param utente utente da aggiungere
     * @return true se l'inserimento ha successo, false altrimenti
     */
    boolean addUtente(Utente utente);

    /**
     * Aggiorna i dati di un utente nel database.
     * @param utente utente con i dati aggiornati
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    boolean updateUtente(Utente utente);

    /**
     * Elimina un utente dal database tramite login.
     * @param login login dell'utente da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean deleteUtente(String login);
}
