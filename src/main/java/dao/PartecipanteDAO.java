package dao;

import implementazionePostgresDAO.ITeamDAO;
import implementazionePostgresDAO.IUtenteDAO;
import model.Partecipante;
import java.util.List;

public interface PartecipanteDAO {
    boolean addPartecipante (String login, int idEvento);
    Partecipante getPartecipante(String login, int eventoId);
    List<Partecipante> getPartecipantiEvento(int eventoId);
    List<Partecipante> getPartecipantiTeam(String nomeTeam, int eventoId);
    boolean aggiungiPartecipante(Partecipante p, int eventoId);
    boolean aggiornaPartecipante(Partecipante p, int eventoId);
    boolean eliminaPartecipante(String login, int eventoId);
    void setUtenteDAO (IUtenteDAO utenteDAO);
    void setTeamDAO (ITeamDAO teamDAO);
    void joinTeam(String loginPartecipante, String nomeTeam, int eventoId);
}
