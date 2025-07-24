package dao;

import implementazione_postgres_dao.IGiudiceDAO;
import model.Voto;
import java.util.List;

public interface VotoDAO {
    Voto getVoto(int id);
    List<Voto> getVotiTeam(String nomeTeam, int eventoId);
    List<Voto> getVotiGiudice(String giudiceLogin);
    boolean aggiungiVoto(Voto voto, int eventoId);
    boolean aggiornaVoto(Voto voto, int id);
    boolean eliminaVoto(int id);
    void setGiudiceDAO (IGiudiceDAO giudiceDAO);
    boolean giudiceHaVotatoTeam (String loginGiudice, String nomeTeam, int eventoId);
    void votaTeam (String loginGiudice, String nomeTeam, int eventoId, int voto);
    int getVotoDiGiudiceTeam (String loginGiudice, String nomeTeam, int eventoId);
}
