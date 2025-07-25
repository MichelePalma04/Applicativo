package dao;

import implementazione_postgres_dao.IUtenteDAO;
import model.Giudice;
import java.util.List;


/**
 * Interfaccia DAO per la gestione dei giudici.
 * Definisce i metodi per il recupero, aggiunta, eliminazione e gestione dei giudici associati agli eventi.
 */
public interface GiudiceDAO {
    /**
     * Restituisce il giudice associato al login e all'evento specificato.
     * @param login login del giudice
     * @param eventoId identificativo dell'evento
     * @return Giudice trovato oppure null se non esiste
     */
    Giudice getGiudice(String login, int eventoId);

    /**
     * Restituisce la lista dei giudici associati ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista dei giudici dell'evento
     */
    List<Giudice> getGiudiciEvento(int eventoId);

    /**
     * Aggiunge un giudice ad un evento.
     * @param login login del giudice da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    boolean aggiungiGiudice(String login, int eventoId);

    /**
     * Elimina un giudice da un evento.
     * @param login login del giudice da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean eliminaGiudice(String login, int eventoId);

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO implementazione del DAO utente
     */
    void setUtenteDAO(IUtenteDAO utenteDAO);
}

