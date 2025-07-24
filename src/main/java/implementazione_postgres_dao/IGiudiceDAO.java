package implementazione_postgres_dao;
import dao.GiudiceDAO;
import model.Giudice;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IGiudiceDAO implements GiudiceDAO {
    private Connection connection;
    private IUtenteDAO utenteDAO;

    public IGiudiceDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Giudice getGiudice(String login, int eventoId) {
        String sql = "SELECT utente_login, evento_id FROM giudice WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = utenteDAO.getUtentebyLogin(login).getPassword();
                return new Giudice(login, password, new ArrayList<>(), null, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Giudice> getGiudiciEvento(int eventoId) {
        List<Giudice> lista = new ArrayList<>();
        String sql = "SELECT utente_login FROM giudice WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getGiudice(rs.getString("utente_login"), eventoId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean aggiungiGiudice(String login, int eventoId) {
        String sql = "INSERT INTO giudice (utente_login, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean eliminaGiudice(String login, int eventoId) {
        String sql = "DELETE FROM giudice WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void setUtenteDAO(IUtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }
}