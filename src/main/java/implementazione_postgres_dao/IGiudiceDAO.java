package implementazione_postgres_dao;
import dao.GiudiceDAO;
import dao.UtenteDAO;
import model.Giudice;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione dei giudici.
 * Fornisce metodi per il recupero, aggiunta ed eliminazione dei giudici associati agli eventi.
 */
public class IGiudiceDAO implements GiudiceDAO {

    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione degli utenti. */
    private UtenteDAO utenteDAO;

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IGiudiceDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce il giudice associato al login e all'evento specificato.
     * @param login login del giudice
     * @param eventoId identificativo dell'evento
     * @return Giudice trovato oppure null se non esiste
     */
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

    /**
     * Restituisce la lista dei giudici associati ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista dei giudici dell'evento
     */
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

    /**
     * Aggiunge un giudice ad un evento.
     * @param login login del giudice da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
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

    /**
     * Elimina un giudice da un evento.
     * @param login login del giudice da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
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

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO istanza di UtenteDAO
     */
    @Override
    public void setUtenteDAO(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }
}