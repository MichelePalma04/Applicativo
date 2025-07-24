package implementazione_postgres_dao;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Team getTeam(String nomeTeam, int eventoId) {
        String sql = "SELECT nome_team, evento_id FROM team WHERE nome_team = ? AND evento_id = ?";
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Team getTeamByNomeEvento(String nomeTeam, int eventoId) {
        return getTeam(nomeTeam, eventoId);
    }

    // Controlla se un partecipante è in un team (con nome+evento)
    @Override
    public boolean isPartecipanteInTeam(String loginPartecipante, String nomeTeam, int eventoId) {
        String sql = "SELECT COUNT(*) FROM team_partecipante WHERE team_nome = ? AND evento_id = ? AND utente_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ps.setString(3, loginPartecipante);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ritorna la dimensione del team (con nome+evento)
    @Override
    public int getDimTeam(String nomeTeam, int eventoId) {
        String sql = "SELECT COUNT(*) FROM partecipante WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public boolean eliminaTeam(String nomeTeam, int eventoId) {
        String sql = "DELETE FROM team WHERE nome_team = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unisciPartecipanteATeam(String loginPartecipante, String nomeTeam, int eventoId) {
        String sql = "INSERT INTO team_partecipante (team_nome, evento_id, utente_login) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ps.setString(3, loginPartecipante);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVotoDAO(IVotoDAO votoDAO) {
        this.votoDAO = votoDAO;
    }

    @Override
    public void setPartecipanteDAO(IPartecipanteDAO partecipanteDAO) {
        this.partecipanteDAO = partecipanteDAO;
    }

}
