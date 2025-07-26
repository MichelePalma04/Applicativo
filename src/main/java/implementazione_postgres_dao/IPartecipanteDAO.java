package implementazione_postgres_dao;
import dao.PartecipanteDAO;
import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Partecipante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione dei partecipanti.
 * Fornisce metodi per il recupero, aggiunta, aggiornamento, eliminazione e gestione dei team dei partecipanti associati agli eventi.
 */
public class IPartecipanteDAO implements PartecipanteDAO{
    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione degli utenti. */
    private UtenteDAO utenteDAO;

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IPartecipanteDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Aggiunge un partecipante ad un evento.
     * @param login login del partecipante da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    @Override
    public boolean addPartecipante (String login, int eventoId) {
        String sql = "INSERT INTO partecipante (utente_login, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restituisce il partecipante associato al login e all'evento specificato.
     * @param login login del partecipante
     * @param eventoId identificativo dell'evento
     * @return Partecipante trovato oppure null se non esiste
     */
    @Override
    public Partecipante getPartecipante(String login, int eventoId) {
        String sql = "SELECT utente_login, evento_id, team_nome FROM partecipante WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = utenteDAO.getUtentebyLogin(login).getPassword();
                String teamNome = rs.getString("team_nome");
                return new Partecipante(login, password, teamNome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Assegna un partecipante a un team per l'evento specificato.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     */
    @Override
    public void joinTeam(String loginPartecipante, String nomeTeam, int eventoId) {
        String sqlUpdate = "UPDATE partecipante SET team_nome = ? WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlUpdate)) {
            ps.setString(1, nomeTeam);
            ps.setString(2, loginPartecipante);
            ps.setInt(3, eventoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce la lista dei partecipanti ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista dei partecipanti dell'evento
     */
    @Override
    public List<Partecipante> getPartecipantiEvento(int eventoId) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT utente_login, evento_id, team_nome FROM partecipante WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getPartecipante(rs.getString("utente_login"), eventoId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Restituisce la lista dei partecipanti di un team per un evento specifico.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return lista dei partecipanti del team
     */
    @Override
    public List<Partecipante> getPartecipantiTeam(String nomeTeam, int eventoId) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT utente_login FROM partecipante WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getPartecipante(rs.getString("utente_login"), eventoId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Aggiunge un partecipante (oggetto) ad un evento specifico, assegnandolo ad un team.
     * @param p partecipante da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    @Override
    public boolean aggiungiPartecipante(Partecipante p, int eventoId) {
        String sql = "INSERT INTO partecipante (utente_login, evento_id, team_nome) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getLogin());
            ps.setInt(2, eventoId);
            ps.setString(3, p.getTeam().getNomeTeam());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Aggiorna i dati di un partecipante per un evento.
     * @param p partecipante da aggiornare
     * @param eventoId identificativo dell'evento
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    @Override
    public boolean aggiornaPartecipante(Partecipante p, int eventoId) {
        String sql = "UPDATE partecipante SET team_nome = ? WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getTeam().getNomeTeam());
            ps.setString(2, p.getLogin());
            ps.setInt(3, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un partecipante da un evento.
     * @param login login del partecipante da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    @Override
    public boolean eliminaPartecipante(String login, int eventoId) {
        String sql = "DELETE FROM partecipante WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Imposta il DAO per la gestione degli utenti.
     * @param utenteDAO istanza di UtenteDAO
     */
    @Override
    public void setUtenteDAO (UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }


}
