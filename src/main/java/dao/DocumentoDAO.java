package dao;
import implementazione_postgres_dao.IDocumentoDAO;
import implementazione_postgres_dao.IGiudiceDAO;
import implementazione_postgres_dao.ITeamDAO;
import model.CommentoGiudice;
import model.Documento;

import java.util.List;

/**
 * Interfaccia DAO per la gestione dei documenti.
 * Definisce i metodi per il recupero, salvataggio e gestione dei documenti associati a team, eventi e partecipanti,
 * oltre alla gestione dei commenti dei giudici sui documenti.
 */
public interface DocumentoDAO {

    /**
     * Restituisce un documento in base al suo identificativo.
     * @param idDocumento identificativo del documento
     * @return documento trovato oppure null se non esiste
     */
    Documento getDocumentoById(int idDocumento);

    /**
     * Salva un documento associato a un team e a un evento.
     * @param documento documento da salvare
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @param login login del partecipante che carica il documento
     */
    void save(Documento documento, String nomeTeam, int eventoId, String login);

    /**
     * Verifica se un team ha documenti per uno specifico evento.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return true se il team ha documenti per l'evento, false altrimenti
     */
    boolean teamHaDocumenti (String nomeTeam, int eventoId);

    /**
     * Restituisce tutti i documenti di un evento.
     * @param eventoId identificativo dell'evento
     * @return lista dei documenti associati all'evento
     */
    List<Documento> getDocumentiEvento(int eventoId);

    /**
     * Restituisce i documenti di un team per un evento e un partecipante.
     * @param eventoId identificativo dell'evento
     * @param nomeTeam nome del team
     * @param login login del partecipante
     * @return lista dei documenti associati al team, evento e partecipante
     */
    List<Documento> getDocumentiTeamEventoPartecipante(int eventoId, String nomeTeam, String login);

    /**
     * Restituisce i documenti di un team per un evento.
     * @param eventoId identificativo dell'evento
     * @param nomeTeam nome del team
     * @return lista dei documenti associati al team e all'evento
     */
    List<Documento> getDocumentiTeamEvento(int eventoId, String nomeTeam);

    /**
     * Imposta il DAO per la gestione dei team.
     * @param teamDAO implementazione del TeamDAO
     */
    void setTeamDAO (ITeamDAO teamDAO);

    /**
     * Restituisce la lista dei commenti dei giudici per un documento e un evento.
     * @param idDocumento identificativo del documento
     * @param eventoId identificativo dell'evento
     * @return lista dei commenti dei giudici
     */
    List<CommentoGiudice> getCommentiDocumento(int idDocumento, int eventoId);

    /**
     * Aggiunge un commento di un giudice ad un documento associato ad un evento.
     * @param idDocumento identificativo del documento
     * @param utenteLogin login del giudice
     * @param eventoId identificativo dell'evento
     * @param testoCommento testo del commento da aggiungere
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     */
    boolean aggiungiCommentoGiudice (int idDocumento, String utenteLogin, int eventoId, String testoCommento);

    /**
     * Imposta il DAO per la gestione dei giudici.
     * @param giudiceDAO implementazione del GiudiceDAO
     */
    void setGiudiceDAO (IGiudiceDAO giudiceDAO);

    /**
     * Imposta il DAO per la gestione dei documenti (self-reference).
     * @param documentoDAO implementazione del DocumentoDAO
     */
    void setDocumentoDAO (IDocumentoDAO documentoDAO);
}
