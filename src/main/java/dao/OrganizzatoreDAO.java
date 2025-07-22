package dao;

import implementazionePostgresDAO.IUtenteDAO;
import model.Organizzatore;
import java.util.List;

public interface OrganizzatoreDAO {
    Organizzatore getOrganizzatore(String login);
    boolean isOrganizzatore (String login);
    List<Organizzatore> getOrganizzatoriEvento(int eventoId);
    boolean aggiungiOrganizzatore(Organizzatore o, int eventoId);
    boolean aggiornaOrganizzatore(Organizzatore o, int eventoId);
    boolean eliminaOrganizzatore(String login, int eventoId);
    void setUtenteDAO(IUtenteDAO utenteDAO);
}
