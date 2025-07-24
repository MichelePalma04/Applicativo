package implementazione_postgres_dao;
import dao.PartecipanteDAO;
import database.ConnessioneDatabase;
import model.Partecipante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IPartecipanteDAO implements PartecipanteDAO{

    private Connection connection;
    private IUtenteDAO utenteDAO;


    public IPartecipanteDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean addPartecipante (String login, int eventoId) {
        String sql = "INSERT INTO partecipante (utente_login, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Partecipante getPartecipante(String login, int eventoId) {
        String sql = "SELECT utente_login, evento_id, team_nome FROM partecipante WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = utenteDAO.getUtentebyLogin(login).getPassword();
                String teamNome = rs.getString("team_nome");
                return new Partecipante(login, password, teamNome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Unisci partecipante a team
    @Override
    public void joinTeam(String loginPartecipante, String nomeTeam, int eventoId) {
        String sqlUpdate = "UPDATE partecipante SET team_nome = ? WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlUpdate)) {
            ps.setString(1, nomeTeam);
            ps.setString(2, loginPartecipante);
            ps.setInt(3, eventoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Partecipante> getPartecipantiEvento(int eventoId) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT utente_login, evento_id, team_nome FROM partecipante WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getPartecipante(rs.getString("utente_login"), eventoId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setUtenteDAO (IUtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }


}
