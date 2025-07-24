package implementazione_postgres_dao;
import dao.*;
import model.Evento;
import model.Organizzatore;
import database.ConnessioneDatabase;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IEventoDAO implements EventoDAO {
    private Connection connection;
    private IOrganizzatoreDAO organizzatoreDAO;


    public IEventoDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Evento getEventoAttivo() {
        String sql = "SELECT id, titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login FROM evento WHERE CURRENT_DATE BETWEEN inizio_registrazioni AND fine_registrazioni LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int eventoId = rs.getInt("id");
                return getEvento(eventoId); // riusa il tuo metodo esistente
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Evento getEvento(int eventoId) {
        String sql = "SELECT id, titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login FROM evento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String titolo = rs.getString("titolo");
                String sede = rs.getString("sede");
                LocalDate dataInizio = rs.getDate("data_inizio").toLocalDate();
                LocalDate dataFine = rs.getDate("data_fine").toLocalDate();
                int nMaxIscritti = rs.getInt("n_max_iscritti");
                int dimMaxTeam = rs.getInt("dim_max_team");
                LocalDate inizioRegistrazioni = rs.getDate("inizio_registrazioni").toLocalDate();
                LocalDate fineRegistrazioni = rs.getDate("fine_registrazioni").toLocalDate();

                Organizzatore organizzatore = organizzatoreDAO.getOrganizzatore(rs.getString("organizzatore_login"));

                return new Evento(id, titolo, sede, dataInizio, dataFine, nMaxIscritti, dimMaxTeam, inizioRegistrazioni, fineRegistrazioni, organizzatore, null, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Evento> getTuttiEventi() {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT id, titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login FROM evento";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id"), rs.getString("titolo"), rs.getString("sede"), rs.getDate("data_inizio").toLocalDate(), rs.getDate("data_fine").toLocalDate(), rs.getInt("n_max_iscritti"), rs.getInt("dim_max_team"), rs.getDate("inizio_registrazioni").toLocalDate(), rs.getDate("fine_registrazioni").toLocalDate(), null, new ArrayList<>(), new ArrayList<>());
                String loginOrg = rs.getString("organizzatore_login");
                Organizzatore organizzatore = organizzatoreDAO.getOrganizzatore(loginOrg);
                evento.setOrganizzatore(organizzatore);
                lista.add(evento);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Evento aggiungiEvento(Evento evento) {
        String sql = "INSERT INTO evento (titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, evento.getTitolo());
            ps.setString(2, evento.getSede());
            ps.setDate(3, java.sql.Date.valueOf(evento.getDataInizio()));
            ps.setDate(4, java.sql.Date.valueOf(evento.getDataFine()));
            ps.setInt(5, evento.getMaxIscritti());
            ps.setInt(6, evento.getDMaxTeam());
            ps.setDate(7, java.sql.Date.valueOf(evento.getInizioReg()));
            ps.setDate(8, java.sql.Date.valueOf(evento.getFineReg()));
            ps.setString(9, evento.getProblema());
            ps.setString(10, evento.getOrganizzatore().getLogin());
            ps.setString(11, evento.getGiudici().isEmpty() ? null : evento.getGiudici().get(0).getLogin());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                evento.setId(rs.getInt("id"));
                return evento;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean aggiornaEvento(Evento evento) {
        String sql = "UPDATE evento SET titolo = ?, sede = ?, data_inizio = ?, data_fine = ?, n_max_iscritti = ?, dim_max_team = ?, inizio_registrazioni = ?, fine_registrazioni = ?, problema = ?, organizzatore_login = ?, giudice_descrizione_login = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, evento.getTitolo());
            ps.setString(2, evento.getSede());
            ps.setDate(3, java.sql.Date.valueOf(evento.getDataInizio()));
            ps.setDate(4, java.sql.Date.valueOf(evento.getDataFine()));
            ps.setInt(5, evento.getMaxIscritti());
            ps.setInt(6, evento.getDMaxTeam());
            ps.setDate(7, java.sql.Date.valueOf(evento.getInizioReg()));
            ps.setDate(8, java.sql.Date.valueOf(evento.getFineReg()));
            ps.setString(9, evento.getProblema());
            ps.setString(10, evento.getOrganizzatore().getLogin());
            ps.setString(11, evento.getGiudici().isEmpty() ? null : evento.getGiudici().get(0).getLogin());
            ps.setInt(12, evento.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminaEvento(int eventoId) {
        String sql = "DELETE FROM evento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Evento> getEventiPerOrganizzatore(String login) {
        List<Evento> eventi = new ArrayList<>();
        String sql = "SELECT id, titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login FROM evento WHERE organizzatore_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id"), rs.getString("titolo"), rs.getString("sede"), rs.getDate("data_inizio").toLocalDate(), rs.getDate("data_fine").toLocalDate(), rs.getInt("n_max_iscritti"), rs.getInt("dim_max_team"), rs.getDate("inizio_registrazioni").toLocalDate(), rs.getDate("fine_registrazioni").toLocalDate(), null, new ArrayList<>(), new ArrayList<>());
                eventi.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventi;
    }

    // Restituisce la descrizione del problema per un evento
    @Override
    public String getProblemaEvento(int eventoId) {
        String sql = "SELECT problema FROM evento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("problema");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Aggiorna la descrizione del problema per un evento
    @Override
    public void setProblemaEvento(int eventoId, String descrizione) {
        String sql = "UPDATE evento SET problema = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, descrizione);
            ps.setInt(2, eventoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Restituisce il login del giudice responsabile
    @Override
    public String getLoginGiudiceDescrizione(int eventoId) {
        String sql = "SELECT giudice_descrizione_login FROM evento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("giudice_descrizione_login");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean setGiudiceDescrizione(int eventoId, String loginGiudice) {
        String sql = "UPDATE evento SET giudice_descrizione_login = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginGiudice);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setOrganizzatoreDAO(IOrganizzatoreDAO organizzatoreDAO) {
        this.organizzatoreDAO = organizzatoreDAO;
    }

}