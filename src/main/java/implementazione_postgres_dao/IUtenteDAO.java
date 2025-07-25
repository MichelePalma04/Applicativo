package implementazione_postgres_dao;

import dao.UtenteDAO;
import model.Utente;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IUtenteDAO implements UtenteDAO {
    private Connection connection;
    private static final String LOGIN_COLUMN = "login";
    private static final String PASSWORD_COLUMN = "password";

    public IUtenteDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

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

