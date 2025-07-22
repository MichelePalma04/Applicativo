package dao;
import implementazionePostgresDAO.IEventoDAO;
import implementazionePostgresDAO.IOrganizzatoreDAO;
import implementazionePostgresDAO.IUtenteDAO;
import implementazionePostgresDAO.IVotoDAO;
import model.Giudice;
import java.util.List;

public interface GiudiceDAO {
    Giudice getGiudice(String login, int eventoId, TeamDAO teamDAO);
    List<Giudice> getGiudiciEvento(int eventoId, TeamDAO teamDAO);
    boolean aggiungiGiudice(Giudice g, int eventoId);
    boolean aggiornaGiudice(Giudice g, int eventoId);
    boolean eliminaGiudice(String login, int eventoId);
    void setEventoDAO(IEventoDAO eventoDAO);
    void setVotoDAO(IVotoDAO votoDAO);
    void setOrganizzatoreDAO(IOrganizzatoreDAO organizzatoreDAO);
    void setUtenteDAO(IUtenteDAO utenteDAO);
}

