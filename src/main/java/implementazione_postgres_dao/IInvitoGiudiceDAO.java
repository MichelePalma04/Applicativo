package implementazione_postgres_dao;

import dao.EventoDAO;
import dao.InvitoGiudiceDAO;
import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Evento;
import model.InvitoGiudice;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione degli inviti ai giudici.
 * Consente l'inserimento, aggiornamento, cancellazione e recupero degli inviti ai giudici associati agli eventi.
 */
public class IInvitoGiudiceDAO implements InvitoGiudiceDAO {
    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione degli eventi. */
    private EventoDAO eventoDAO;

    /** DAO per la gestione degli utenti. */
    private UtenteDAO utenteDAO;

    /** Nome della colonna evento_id nella tabella invito_giudice. */
    private static final String EVENTOID_COLUMN = "evento_id";

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IInvitoGiudiceDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aggiunge un nuovo invito per un giudice.
     * @param invito invito da aggiungere
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    @Override
    public boolean addInvitoGiudice(InvitoGiudice invito) {
        String sql = "INSERT INTO invito_giudice (evento_id, utente_login, accettato, rifiutato) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invito.getEvento().getId());
            ps.setString(2, invito.getUtente().getLogin());
            ps.setBoolean(3, invito.isAccettato());
            ps.setBoolean(4, invito.isRifiutato());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restituisce la lista degli inviti pendenti per uno specifico utente.
     * @param loginUtente login dell'utente
     * @return lista degli inviti pendenti
     */
    @Override
    public List<InvitoGiudice> getInvitiPendentiPerUtente(String loginUtente) {
        List<InvitoGiudice> lista = new ArrayList<>();
        String sql = "SELECT id, evento_id, utente_login, accettato, rifiutato FROM invito_giudice WHERE utente_login = ? AND accettato = false AND rifiutato = false";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Evento evento = eventoDAO.getEvento(rs.getInt(EVENTOID_COLUMN));
                Utente utente = utenteDAO.getUtentebyLogin(rs.getString("utente_login"));
                InvitoGiudice invito = new InvitoGiudice(rs.getInt(EVENTOID_COLUMN), rs.getInt("id"), evento, utente, rs.getBoolean("accettato"), rs.getBoolean("rifiutato"));
                lista.add(invito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Restituisce l'invito tramite identificativo.
     * @param idInvito identificativo dell'invito
     * @return invito trovato oppure null se non esiste
     */
    @Override
    public InvitoGiudice getInvitoById(int idInvito) {
        String sql = "SELECT id, evento_id, utente_login, accettato, rifiutato  FROM invito_giudice WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idInvito);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Evento evento = eventoDAO.getEvento(rs.getInt(EVENTOID_COLUMN));
                Utente utente = utenteDAO.getUtentebyLogin(rs.getString("utente_login"));
                boolean accettato = rs.getBoolean("accettato");
                boolean rifiutato = rs.getBoolean("rifiutato");
                return new InvitoGiudice(rs.getInt(EVENTOID_COLUMN), rs.getInt("id"), evento, utente, accettato, rifiutato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica se esiste un invito pendente per uno specifico utente e evento.
     * @param login login dell'utente
     * @param eventoId identificativo dell'evento
     * @return true se esiste almeno un invito pendente, false altrimenti
     */
    @Override
    public boolean esisteInvitoPendentePerUtenteEvento(String login, int eventoId) {
        String sql = "SELECT 1 FROM invito_giudice WHERE utente_login = ? AND evento_id = ? AND accettato = false AND rifiutato = false LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aggiorna lo stato di un invito giudice (accettato/rifiutato).
     * @param invito invito da aggiornare
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    @Override
    public boolean updateInvitoGiudice(InvitoGiudice invito) {
        String sql = "UPDATE invito_giudice SET accettato = ?, rifiutato = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, invito.isAccettato());
            ps.setBoolean(2, invito.isRifiutato());
            ps.setInt(3, invito.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un invito giudice dal database.
     * @param idInvito identificativo dell'invito da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
    @Override
    public boolean deleteInvitoGiudice(int idInvito) {
        String sql = "DELETE FROM invito_giudice WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idInvito);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO implementazione del DAO utente
     */
    @Override
    public void setUtenteDAO(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /**
     * Imposta il DAO per la gestione degli eventi.
     * @param eventoDAO implementazione del DAO evento
     */
    @Override
    public void setEventoDAO(EventoDAO eventoDAO){
        this.eventoDAO = eventoDAO;
    }


}
