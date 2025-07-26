package implementazione_postgres_dao;
import dao.*;
import model.Evento;
import model.Organizzatore;
import database.ConnessioneDatabase;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione degli eventi.
 * Fornisce metodi per il salvataggio, recupero, aggiornamento ed eliminazione degli eventi,
 * oltre a gestire la descrizione del problema e il giudice responsabile della descrizione.
 */
public class IEventoDAO implements EventoDAO {

    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione degli organizzatori. */
    private OrganizzatoreDAO organizzatoreDAO;

    /** Nome della colonna titolo nella tabella evento. */
    private static final String TITOLO_COLUMN = "titolo";

    /** Nome della colonna sede nella tabella evento. */
    private static final String SEDE_COLUMN = "sede";

    /** Nome della colonna data_inizio nella tabella evento. */
    private static final String DATA_INIZIO_COLUMN = "data_inizio";

    /** Nome della colonna data_fine nella tabella evento. */
    private static final String DATA_FINE_COLUMN = "data_fine";

    /** Nome della colonna n_max_iscritti nella tabella evento. */
    private static final String MAX_ISCRITTI_COLUMN = "n_max_iscritti";

    /** Nome della colonna dim_max_team nella tabella evento. */
    private static final String DIM_TEAM_COLUMN = "dim_max_team";

    /** Nome della colonna inizio_registrazioni nella tabella evento. */
    private static final String INIZIO_REG_COLUMN = "inizio_registrazioni";

    /** Nome della colonna fine_registrazioni nella tabella evento. */
    private static final String FINE_REG_COLUMN = "fine_registrazioni";


    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IEventoDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce l'evento attivo, cioè quello in cui le registrazioni sono aperte.
     * @return evento attivo o null se non esiste
     */
    @Override
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

    /**
     * Restituisce l'evento corrispondente all'id fornito.
     * @param eventoId identificativo dell'evento
     * @return evento trovato o null se non esiste
     */
    @Override
    public Evento getEvento(int eventoId) {
        String sql = "SELECT id, titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login FROM evento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String titolo = rs.getString(TITOLO_COLUMN);
                String sede = rs.getString(SEDE_COLUMN);
                LocalDate dataInizio = rs.getDate(DATA_INIZIO_COLUMN).toLocalDate();
                LocalDate dataFine = rs.getDate(DATA_FINE_COLUMN).toLocalDate();
                int nMaxIscritti = rs.getInt(MAX_ISCRITTI_COLUMN);
                int dimMaxTeam = rs.getInt(DIM_TEAM_COLUMN);
                LocalDate inizioRegistrazioni = rs.getDate(INIZIO_REG_COLUMN).toLocalDate();
                LocalDate fineRegistrazioni = rs.getDate(FINE_REG_COLUMN).toLocalDate();

                Organizzatore organizzatore = organizzatoreDAO.getOrganizzatore(rs.getString("organizzatore_login"));

                return new Evento(id, titolo, sede, dataInizio, dataFine, nMaxIscritti, dimMaxTeam, inizioRegistrazioni, fineRegistrazioni, organizzatore, null, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Restituisce la lista di tutti gli eventi presenti nel database.
     * @return lista di eventi
     */
    @Override
    public List<Evento> getTuttiEventi() {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT id, titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login FROM evento";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id"), rs.getString(TITOLO_COLUMN), rs.getString(SEDE_COLUMN), rs.getDate(DATA_INIZIO_COLUMN ).toLocalDate(), rs.getDate(DATA_FINE_COLUMN).toLocalDate(), rs.getInt(MAX_ISCRITTI_COLUMN), rs.getInt(DIM_TEAM_COLUMN), rs.getDate(INIZIO_REG_COLUMN).toLocalDate(), rs.getDate(FINE_REG_COLUMN).toLocalDate(), null, new ArrayList<>(), new ArrayList<>());
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

    /**
     * Aggiunge un nuovo evento al database.
     * @param evento evento da aggiungere
     * @return evento aggiunto con id valorizzato, o null se si verifica un errore
     */
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

    /**
     * Aggiorna i dati di un evento esistente.
     * @param evento evento con i dati aggiornati
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
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

    /**
     * Elimina un evento dal database.
     * @param eventoId id dell'evento da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
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

    /**
     * Restituisce la lista degli eventi organizzati da uno specifico organizzatore.
     * @param login login dell'organizzatore
     * @return lista di eventi organizzati dall'organizzatore
     */
    @Override
    public List<Evento> getEventiPerOrganizzatore(String login) {
        List<Evento> eventi = new ArrayList<>();
        String sql = "SELECT id, titolo, sede, data_inizio, data_fine, n_max_iscritti, dim_max_team, inizio_registrazioni, fine_registrazioni, problema, organizzatore_login, giudice_descrizione_login FROM evento WHERE organizzatore_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id"), rs.getString(TITOLO_COLUMN), rs.getString(SEDE_COLUMN), rs.getDate(DATA_INIZIO_COLUMN ).toLocalDate(), rs.getDate(DATA_FINE_COLUMN).toLocalDate(), rs.getInt(MAX_ISCRITTI_COLUMN), rs.getInt(DIM_TEAM_COLUMN), rs.getDate(INIZIO_REG_COLUMN).toLocalDate(), rs.getDate(FINE_REG_COLUMN).toLocalDate(), null, new ArrayList<>(), new ArrayList<>());
                eventi.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventi;
    }

    /**
     * Restituisce la descrizione del problema proposto per uno specifico evento.
     * @param eventoId id dell'evento
     * @return descrizione del problema
     */
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

    /**
     * Aggiorna la descrizione del problema per uno specifico evento.
     * @param eventoId id dell'evento
     * @param descrizione nuova descrizione del problema
     */
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

    /**
     * Restituisce il login del giudice responsabile della descrizione del problema per un evento.
     * @param eventoId id dell'evento
     * @return login del giudice responsabile della descrizione
     */
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

    /**
     * Aggiorna il giudice responsabile della descrizione del problema per un evento.
     * @param eventoId id dell'evento
     * @param loginGiudice login del giudice da impostare
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
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

    /**
     * Imposta il DAO per la gestione degli organizzatori.
     * @param organizzatoreDAO istanza di OrganizzatoreDAO
     */
    @Override
    public void setOrganizzatoreDAO(OrganizzatoreDAO organizzatoreDAO) {
        this.organizzatoreDAO = organizzatoreDAO;
    }

}