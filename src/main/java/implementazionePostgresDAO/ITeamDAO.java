package implementazionePostgresDAO;

import dao.TeamDAO;
import model.Partecipante;
import model.Team;
import database.ConnessioneDatabase;
import model.Voto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ITeamDAO implements TeamDAO {

    private Connection connection;
    private IPartecipanteDAO partecipanteDAO;
    private IVotoDAO votoDAO;

    public ITeamDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
            this.partecipanteDAO = new IPartecipanteDAO();
            this.votoDAO = new IVotoDAO();
        } catch (SQLException e) {
            System.out.println("Errore nella connessione al database: " + e.getMessage());
        }
    }

    @Override
    public Team getTeam(String nomeTeam, int eventoId) {
        String sql = "SELECT * FROM team WHERE nome_team = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ArrayList<Partecipante> partecipanti = new ArrayList<>(partecipanteDAO.getPartecipantiTeam(nomeTeam, eventoId));
                ArrayList <Voto> voto = new ArrayList <>(votoDAO.getVotiTeam(nomeTeam, eventoId));
                return new Team(nomeTeam, partecipanti, voto);
            }
        } catch (SQLException e) {
            System.out.println("Errore nella ricerca team: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Team> getTeamEvento(int eventoId) {
        List<Team> lista = new ArrayList<>();
        String sql = "SELECT nome_team FROM team WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getTeam(rs.getString("nome_team"), eventoId));
            }
        } catch (SQLException e) {
            System.out.println("Errore nel recupero team: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean aggiungiTeam(Team team, int eventoId) {
        String sql = "INSERT INTO team (nome_team, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, team.getNomeTeam());
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore nell'inserimento team: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminaTeam(String nomeTeam, int eventoId) {
        String sql = "DELETE FROM team WHERE nome_team = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore nell'eliminazione team: " + e.getMessage());
            return false;
        }
    }
}
