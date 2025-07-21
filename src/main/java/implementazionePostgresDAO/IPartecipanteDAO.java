package implementazionePostgresDAO;
import dao.PartecipanteDAO;
import database.ConnessioneDatabase;
import model.Evento;
import model.Partecipante;
import model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IPartecipanteDAO implements PartecipanteDAO{

    private Connection connection;
    private IUtenteDAO utenteDAO;
    private ITeamDAO teamDAO;
    private IEventoDAO eventoDAO;

    public IPartecipanteDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
            this.utenteDAO = utenteDAO;
            this.teamDAO = teamDAO;
            this.eventoDAO = eventoDAO;
        }catch(SQLException e){
            System.out.println("Errore nella connessione al database: "+ e.getMessage());
        }
    }

    public boolean addPartecipante (String login, int evento_id) {
        String sql = "INSERT INTO partecipante (utente_login, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, evento_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Partecipante getPartecipante(String login, int evento_id) {
        String sql = "SELECT * FROM partecipante WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, evento_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = utenteDAO.getUtentebyLogin(login).getPassword();
                Team team = teamDAO.getTeam(rs.getString("team_nome"), evento_id);
                ArrayList<Evento> eventi = new ArrayList<>();
                eventi.add(eventoDAO.getEvento(evento_id));
                return new Partecipante(login, password, team, eventi);
            }
        } catch (SQLException e) {}
        return null;
    }

    @Override
    public List<Partecipante> getPartecipantiEvento(int eventoId) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT * FROM partecipante WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getPartecipante(rs.getString("utente_login"), eventoId));
            }
        } catch (SQLException e) {
            System.out.println("Errore nel recupero partecipanti: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Partecipante> getPartecipantiTeam(String nomeTeam, int eventoId) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT utente_login FROM partecipante WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getPartecipante(rs.getString("utente_login"), eventoId));
            }
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public boolean aggiungiPartecipante(Partecipante p, int eventoId) {
        String sql = "INSERT INTO partecipante (utente_login, evento_id, team_nome) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getLogin());
            ps.setInt(2, eventoId);
            ps.setString(3, p.getTeam().getNomeTeam());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {}
        return false;
    }


    @Override
    public boolean aggiornaPartecipante(Partecipante p, int eventoId) {
        String sql = "UPDATE partecipante SET team_nome = ? WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getTeam().getNomeTeam());
            ps.setString(2, p.getLogin());
            ps.setInt(3, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {}
        return false;
    }

    @Override
    public boolean eliminaPartecipante(String login, int eventoId) {
        String sql = "DELETE FROM partecipante WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore nell'eliminazione partecipante: " + e.getMessage());
            return false;
        }
    }
}
