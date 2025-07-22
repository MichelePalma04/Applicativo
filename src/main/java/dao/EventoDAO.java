package dao;

import implementazionePostgresDAO.IGiudiceDAO;
import implementazionePostgresDAO.IOrganizzatoreDAO;
import implementazionePostgresDAO.IPartecipanteDAO;
import implementazionePostgresDAO.ITeamDAO;
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
    void setGiudiceDAO (IGiudiceDAO giudiceDAO);
    void setPartecipanteDAO (IPartecipanteDAO partecipanteDAO);
    void setTeamDAO (ITeamDAO teamDAO);
}
