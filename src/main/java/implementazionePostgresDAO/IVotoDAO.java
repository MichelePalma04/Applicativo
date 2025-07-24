package implementazionePostgresDAO;

import dao.GiudiceDAO;
import dao.PartecipanteDAO;
import dao.VotoDAO;
import model.Voto;
import model.Giudice;
import model.Team;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IVotoDAO implements VotoDAO {
    private Connection connection;
    private IGiudiceDAO giudiceDAO;
    private ITeamDAO teamDAO;

    public IVotoDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
            //this.giudiceDAO = giudiceDAO;
            //this.teamDAO = teamDAO;
        } catch (SQLException e) {}
    }

    @Override
    public Voto getVoto(int id) {
        String sql = "SELECT * FROM voto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Giudice giudice = giudiceDAO.getGiudice(rs.getString("giudice_login"), rs.getInt("evento_id"));
                //Team team = teamDAO.getTeam(rs.getString("team_nome"), rs.getInt("evento_id"));
                Team team = new Team(rs.getString("team_nome"), new ArrayList<>(), new ArrayList<>());
                int votazione = rs.getInt("votazione");
                return new Voto(giudice, team, votazione);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Voto> getVotiTeam(String nomeTeam, int eventoId) {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT id FROM voto WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getVoto(rs.getInt("id")));
            }
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public List<Voto> getVotiGiudice(String giudiceLogin) {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT id, evento_id FROM voto WHERE giudice_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giudiceLogin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getVoto(rs.getInt("id")));
            }
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public boolean aggiungiVoto(Voto voto, int eventoId) {
        String sql = "INSERT INTO voto (giudice_login, team_nome, evento_id, votazione) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, voto.getGiudice().getLogin());
            ps.setString(2, voto.getTeam().getNomeTeam());
            ps.setInt(3, eventoId); // supponendo che ogni team abbia almeno un partecipante con almeno un evento
            ps.setInt(4, voto.getVotazione());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {}
        return false;
    }

    @Override
    public boolean aggiornaVoto(Voto voto, int id) {
        String sql = "UPDATE voto SET votazione = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, voto.getVotazione());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {}
        return false;
    }

    @Override
    public boolean eliminaVoto(int id) {
        String sql = "DELETE FROM voto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {}
        return false;
    }

    // Verifica se il giudice ha votato il team
    @Override
    public boolean giudiceHaVotatoTeam(String loginGiudice, String nomeTeam, int eventoId) {
        String sql = "SELECT COUNT(*) AS cnt FROM voto WHERE giudice_login = ? AND team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginGiudice);
            ps.setString(2, nomeTeam);
            ps.setInt(3, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("cnt") > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Restituisce il voto assegnato
    @Override
    public int getVotoDiGiudiceTeam(String loginGiudice, String nomeTeam, int eventoId) {
        String sql = "SELECT votazione FROM voto WHERE giudice_login = ? AND team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginGiudice);
            ps.setString(2, nomeTeam);
            ps.setInt(3, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("votazione");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1; // Nessun voto trovato
    }

    // Registra il voto
    @Override
    public void votaTeam(String loginGiudice, String nomeTeam, int eventoId, int voto) {
        String sql = "INSERT INTO voto (giudice_login, team_nome, evento_id, votazione) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginGiudice);
            ps.setString(2, nomeTeam);
            ps.setInt(3, eventoId);
            ps.setInt(4, voto);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }


    @Override
    public void setGiudiceDAO(IGiudiceDAO giudiceDAO) {
        this.giudiceDAO = giudiceDAO;
    }

    @Override
    public void setTeamDAO(ITeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }
}
