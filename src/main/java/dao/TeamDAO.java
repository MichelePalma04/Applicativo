package dao;

import model.Team;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei team.
 * Definisce i metodi per il recupero, aggiunta, eliminazione e gestione dei team e dei partecipanti associati agli eventi,
 * oltre alla gestione dei voti e delle relazioni tra team e partecipanti.
 */
public interface TeamDAO {
    /**
     * Restituisce il team associato al nome e all'evento specificato.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return Team trovato oppure null se non esiste
     */
    Team getTeam(String nomeTeam, int eventoId);

    /**
     * Restituisce la lista di tutti i team associati ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista dei team dell'evento
     */
    List<Team> getTeamEvento(int eventoId);

    /**
     * Aggiunge un nuovo team ad un evento.
     * @param team team da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    boolean aggiungiTeam(Team team, int eventoId);

    /**
     * Elimina un team associato ad un evento.
     * @param nomeTeam nome del team da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    boolean eliminaTeam(String nomeTeam, int eventoId);

    /**
     * Verifica se un partecipante appartiene a un team per un evento specifico.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return true se il partecipante è nel team, false altrimenti
     */
    boolean isPartecipanteInTeam(String loginPartecipante, String nomeTeam, int eventoId);

    /**
     * Restituisce la dimensione (numero di partecipanti) di un team per un evento specifico.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return dimensione del team
     */
    int getDimTeam(String nomeTeam, int eventoId);

    /**
     * Imposta il DAO per la gestione dei voti.
     * @param votoDAO istanza di VotoDAO
     */
    void setVotoDAO(VotoDAO votoDAO);

    /**
     * Imposta il DAO per la gestione dei partecipanti.
     * @param partecipanteDAO istanza di PartecipanteDAO
     */
    void setPartecipanteDAO(PartecipanteDAO partecipanteDAO);

    /**
     * Aggiunge un partecipante ad un team.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     */
    void unisciPartecipanteATeam(String loginPartecipante, String nomeTeam, int eventoId);
}