package dao;
import implementazionePostgresDAO.IEventoDAO;
import implementazionePostgresDAO.IOrganizzatoreDAO;
import implementazionePostgresDAO.IUtenteDAO;
import implementazionePostgresDAO.IVotoDAO;
import model.Giudice;
import java.util.List;

public interface GiudiceDAO {
    Giudice getGiudice(String login, int eventoId);
    List<Giudice> getGiudiciEvento(int eventoId);
    boolean aggiungiGiudice(String login, int eventoId);
    boolean aggiornaGiudice(Giudice g, int eventoId);
    boolean eliminaGiudice(String login, int eventoId);
    void setEventoDAO(IEventoDAO eventoDAO);
    void setVotoDAO(IVotoDAO votoDAO);
    void setOrganizzatoreDAO(IOrganizzatoreDAO organizzatoreDAO);
    void setUtenteDAO(IUtenteDAO utenteDAO);
}

