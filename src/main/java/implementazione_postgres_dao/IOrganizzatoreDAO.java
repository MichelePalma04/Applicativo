package implementazione_postgres_dao;
import dao.OrganizzatoreDAO;
import dao.UtenteDAO;
import model.Organizzatore;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione degli organizzatori.
 * Fornisce metodi per il recupero, aggiunta, eliminazione e verifica degli organizzatori associati agli eventi.
 */
public class IOrganizzatoreDAO implements OrganizzatoreDAO {

    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione degli utenti. */
    private UtenteDAO utenteDAO;

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IOrganizzatoreDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce l'organizzatore associato al login specificato.
     * @param login login dell'organizzatore
     * @return Organizzatore trovato oppure null se non esiste
     */
    @Override
    public Organizzatore getOrganizzatore(String login) {
        String sql = "SELECT utente_login FROM organizzatore WHERE utente_login = ?";
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

    /**
     * Restituisce la lista degli organizzatori associati ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista degli organizzatori dell'evento
     */
    @Override
    public List<Organizzatore> getOrganizzatoriEvento(int eventoId) {
        List<Organizzatore> lista = new ArrayList<>();
        String sql = "SELECT utente_login FROM organizzatore_evento WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getOrganizzatore(rs.getString("utente_login")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Aggiunge un organizzatore ad un evento.
     * @param o organizzatore da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    @Override
    public boolean aggiungiOrganizzatore(Organizzatore o, int eventoId) {
        String sql = "INSERT INTO organizzatore_evento (utente_login, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, o.getLogin());
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un organizzatore associato ad un evento.
     * @param login login dell'organizzatore da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    @Override
    public boolean eliminaOrganizzatore(String login, int eventoId) {
        String sql = "DELETE FROM organizzatore_evento WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica se l'utente con il login specificato è un organizzatore.
     * @param login login dell'utente da verificare
     * @return true se l'utente è un organizzatore, false altrimenti
     */
    @Override
    public boolean isOrganizzatore(String login) {
        String sql = "SELECT COUNT(*) FROM organizzatore WHERE utente_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO implementazione del DAO utente
     */
    @Override
    public void setUtenteDAO(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }
}
