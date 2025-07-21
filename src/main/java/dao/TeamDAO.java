package dao;
import model.Team;
import java.util.List;

public interface TeamDAO {
    Team getTeam(String nomeTeam, int eventoId);
    List<Team> getTeamEvento(int eventoId);
    boolean aggiungiTeam(Team team, int eventoId);
    boolean eliminaTeam(String nomeTeam, int eventoId);
}