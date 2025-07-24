package dao;

import implementazione_postgres_dao.IGiudiceDAO;
import implementazione_postgres_dao.IOrganizzatoreDAO;
import implementazione_postgres_dao.IPartecipanteDAO;
import implementazione_postgres_dao.ITeamDAO;
import model.Evento;
import java.util.List;

public interface EventoDAO {
    List<Evento> getEventiPerOrganizzatore(String login);
    Evento getEventoAttivo();
    Evento getEvento(int eventoId);
    List<Evento> getTuttiEventi();
    Evento aggiungiEvento(Evento evento);
    boolean aggiornaEvento(Evento evento);
    boolean eliminaEvento(int eventoId);
    void setOrganizzatoreDAO (IOrganizzatoreDAO organizzatoreDAO);
    String getProblemaEvento(int eventoId);
    void setProblemaEvento(int eventoId, String descrizione);
    String getLoginGiudiceDescrizione (int eventoId);
    boolean setGiudiceDescrizione (int eventoId, String loginGiudice);
}
