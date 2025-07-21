package dao;
import model.Giudice;
import java.util.List;

public interface GiudiceDAO {
    Giudice getGiudice(String login, int eventoId);
    List<Giudice> getGiudiciEvento(int eventoId);
    boolean aggiungiGiudice(Giudice g, int eventoId);
    boolean aggiornaGiudice(Giudice g, int eventoId);
    boolean eliminaGiudice(String login, int eventoId);
}

