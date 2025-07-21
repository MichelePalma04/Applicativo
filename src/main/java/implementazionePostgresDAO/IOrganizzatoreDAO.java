package implementazionePostgresDAO;
import dao.OrganizzatoreDAO;
import model.Organizzatore;
import model.Evento;
import model.Giudice;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IOrganizzatoreDAO implements OrganizzatoreDAO {
    private Connection connection;
    private IUtenteDAO utenteDAO;
    private IEventoDAO eventoDAO;
    private IGiudiceDAO giudiceDAO;

    public IOrganizzatoreDAO( ) {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
            this.utenteDAO = utenteDAO;
            this.eventoDAO = eventoDAO;
            this.giudiceDAO = giudiceDAO;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Organizzatore getOrganizzatore(String login) {
        String sql = "SELECT * FROM organizzatore WHERE utente_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = utenteDAO.getUtentebyLogin(login).getPassword();
                return new Organizzatore(login, password, new ArrayList<>(), new ArrayList<>());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Organizzatore> getOrganizzatoriEvento(int eventoId) {
        List<Organizzatore> lista = new ArrayList<>();
        String sql = "SELECT utente_login FROM organizzatore WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getOrganizzatore(rs.getString("utente_login")));
            }
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public boolean aggiungiOrganizzatore(Organizzatore o, int eventoId) {
        String sql = "INSERT INTO organizzatore (utente_login, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, o.getLogin());
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {}
        return false;
    }

    @Override
    public boolean aggiornaOrganizzatore(Organizzatore o, int eventoId) {
        // Di solito non serve aggiornare la tabella organizzatore, solo aggiungere o eliminare
        return false;
    }

    @Override
    public boolean eliminaOrganizzatore(String login, int eventoId) {
        String sql = "DELETE FROM organizzatore WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {}
        return false;
    }

    @Override
    public boolean isOrganizzatore(String login) {
        String sql = "SELECT COUNT(*) FROM organizzatore WHERE utente_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
