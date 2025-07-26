package dao;

import model.Organizzatore;
import java.util.List;

/**
 * Interfaccia DAO per la gestione degli organizzatori.
 * Definisce i metodi per il recupero, aggiunta, eliminazione e verifica degli organizzatori associati agli eventi.
 */
public interface OrganizzatoreDAO {

    /**
     * Restituisce l'organizzatore associato al login specificato.
     * @param login login dell'organizzatore
     * @return Organizzatore trovato oppure null se non esiste
     */
    Organizzatore getOrganizzatore(String login);

    /**
     * Verifica se l'utente con il login specificato è un organizzatore.
     * @param login login dell'utente da verificare
     * @return true se l'utente è un organizzatore, false altrimenti
     */
    boolean isOrganizzatore (String login);

    /**
     * Restituisce la lista degli organizzatori associati ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista degli organizzatori dell'evento
     */
    List<Organizzatore> getOrganizzatoriEvento(int eventoId);

    /**
     * Aggiunge un organizzatore ad un evento.
     * @param o organizzatore da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    boolean aggiungiOrganizzatore(Organizzatore o, int eventoId);

    /**
     * Elimina un organizzatore associato ad un evento.
     * @param login login dell'organizzatore da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean eliminaOrganizzatore(String login, int eventoId);

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO istanza di UtenteDAO
     */
    void setUtenteDAO(UtenteDAO utenteDAO);
}
