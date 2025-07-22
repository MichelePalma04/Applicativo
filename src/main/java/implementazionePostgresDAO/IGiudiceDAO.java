package implementazionePostgresDAO;
import dao.GiudiceDAO;
import dao.PartecipanteDAO;
import dao.TeamDAO;
import model.Giudice;
import model.Evento;
import model.Voto;
import model.Organizzatore;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IGiudiceDAO implements GiudiceDAO {
    private Connection connection;
    private IUtenteDAO utenteDAO;
    private IEventoDAO eventoDAO;
    private IVotoDAO votoDAO = new IVotoDAO();
    private IOrganizzatoreDAO organizzatoreDAO = new IOrganizzatoreDAO ();

    public IGiudiceDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
           // this.utenteDAO = utenteDAO;
           // this.eventoDAO = eventoDAO;
           // this.votoDAO = votoDAO;
           // this.organizzatoreDAO = organizzatoreDAO;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Giudice getGiudice(String login, int eventoId, TeamDAO teamDAO) {
        String sql = "SELECT * FROM giudice WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = utenteDAO.getUtentebyLogin(login).getPassword();
                ArrayList<Evento> eventi = new ArrayList<>();
                eventi.add(eventoDAO.getEvento(eventoId));
                ArrayList<Voto> voti = new ArrayList<>(votoDAO.getVotiGiudice(login));
                ArrayList<Organizzatore> organizzatori = new ArrayList<>(organizzatoreDAO.getOrganizzatoriEvento(eventoId));
                return new Giudice(login, password, eventi, voti, organizzatori);
            }
        } catch (SQLException e) {}
        return null;
    }

    @Override
    public List<Giudice> getGiudiciEvento(int eventoId, TeamDAO teamDAO) {
        List<Giudice> lista = new ArrayList<>();
        String sql = "SELECT utente_login FROM giudice WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getGiudice(rs.getString("utente_login"), eventoId, teamDAO));
            }
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public boolean aggiungiGiudice(Giudice g, int eventoId) {
        String sql = "INSERT INTO giudice (utente_login, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, g.getLogin());
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {}
        return false;
    }

    @Override
    public boolean aggiornaGiudice(Giudice g, int eventoId) {
        // Di solito non serve aggiornare la tabella giudice, solo aggiungere o eliminare
        return false;
    }

    @Override
    public boolean eliminaGiudice(String login, int eventoId) {
        String sql = "DELETE FROM giudice WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {}
        return false;
    }

    @Override
    public void setEventoDAO(IEventoDAO eventoDAO) {
        this.eventoDAO = eventoDAO;
    }

    @Override
    public void setVotoDAO(IVotoDAO votoDAO) {
        this.votoDAO = votoDAO;
    }

    @Override
    public void setOrganizzatoreDAO (IOrganizzatoreDAO organizzatoreDAO) {
        this.organizzatoreDAO = organizzatoreDAO;
    }

    @Override
    public void setUtenteDAO(IUtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }
}