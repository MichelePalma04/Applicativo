package dao;
import implementazione_postgres_dao.IEventoDAO;
import implementazione_postgres_dao.IOrganizzatoreDAO;
import implementazione_postgres_dao.IUtenteDAO;
import implementazione_postgres_dao.IVotoDAO;
import model.Giudice;
import java.util.List;

public interface GiudiceDAO {
    Giudice getGiudice(String login, int eventoId);
    List<Giudice> getGiudiciEvento(int eventoId);
    boolean aggiungiGiudice(String login, int eventoId);
    boolean eliminaGiudice(String login, int eventoId);
    void setUtenteDAO(IUtenteDAO utenteDAO);
}

