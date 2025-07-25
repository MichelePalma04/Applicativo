package implementazione_postgres_dao;

import dao.UtenteDAO;
import model.Utente;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione degli utenti.
 * Fornisce metodi per il recupero, aggiunta, aggiornamento ed eliminazione degli utenti nel database.
 */
public class IUtenteDAO implements UtenteDAO {
    /** Connessione al database. */
    private Connection connection;

    /** Nome della colonna login nella tabella utente. */
    private static final String LOGIN_COLUMN = "login";

    /** Nome della colonna password nella tabella utente. */
    private static final String PASSWORD_COLUMN = "password";

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IUtenteDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Aggiunge un nuovo utente al database.
     * @param utente utente da aggiungere
     * @return true se l'inserimento ha successo, false altrimenti
     */
    @Override
    public boolean addUtente(Utente utente) {
        String sql = "INSERT INTO utente (login, password) VALUES (?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString (1, utente.getLogin());
            ps.setString(2, utente.getPassword());
            ps.executeUpdate();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Recupera un utente dal database tramite login.
     * @param login login dell'utente da cercare
     * @return Utente trovato oppure null se non esiste
     */
    @Override
    public Utente getUtentebyLogin (String login) {
        String sql = "SELECT login, password FROM utente WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Utente(rs.getString(LOGIN_COLUMN), rs.getString(PASSWORD_COLUMN));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recupera un utente dal database tramite login e password.
     * @param login login dell'utente
     * @param password password dell'utente
     * @return Utente trovato oppure null se non esiste
     */
    @Override
    public Utente getUtentebyLoginAndPassword (String login, String password) {
        String sql = "SELECT login, password FROM utente WHERE login = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Utente(rs.getString(LOGIN_COLUMN), rs.getString(PASSWORD_COLUMN));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Restituisce la lista di tutti gli utenti presenti nel database.
     * @return lista di utenti
     */
    @Override
    public List<Utente> getAllUtenti() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT login, password FROM utente";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                utenti.add(new Utente(rs.getString(LOGIN_COLUMN), rs.getString(PASSWORD_COLUMN)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    /**
     * Elimina un utente dal database tramite login.
     * @param login login dell'utente da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    @Override
    public boolean deleteUtente (String login) {
        String sql = "DELETE FROM utente WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aggiorna la password di un utente nel database.
     * @param utente utente con i dati aggiornati
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    @Override
    public boolean updateUtente(Utente utente) {
        String sql = "UPDATE utente SET password = ? WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, utente.getPassword());
            ps.setString(2, utente.getLogin());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

