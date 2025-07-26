package implementazione_postgres_dao;

import dao.GiudiceDAO;
import dao.VotoDAO;
import model.Voto;
import model.Giudice;
import model.Team;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione dei voti.
 * Fornisce metodi per il recupero, aggiunta, aggiornamento ed eliminazione dei voti assegnati dai giudici ai team negli eventi.
 */
public class IVotoDAO implements VotoDAO {
    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione dei giudici. */
    private GiudiceDAO giudiceDAO;

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IVotoDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce il voto tramite identificativo.
     * @param id identificativo del voto
     * @return Voto trovato oppure null se non esiste
     */
    @Override
    public Voto getVoto(int id) {
        String sql = "SELECT id, giudice_login, team_nome, evento_id, votazione FROM voto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Giudice giudice = giudiceDAO.getGiudice(rs.getString("giudice_login"), rs.getInt("evento_id"));
                Team team = new Team(rs.getString("team_nome"), new ArrayList<>(), new ArrayList<>());
                int votazione = rs.getInt("votazione");
                return new Voto(giudice, team, votazione);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Restituisce la lista dei voti assegnati ad un team per un evento.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return lista dei voti del team
     */
    @Override
    public List<Voto> getVotiTeam(String nomeTeam, int eventoId) {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT id FROM voto WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getVoto(rs.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Restituisce la lista dei voti assegnati da un giudice.
     * @param giudiceLogin login del giudice
     * @return lista dei voti assegnati dal giudice
     */
    @Override
    public List<Voto> getVotiGiudice(String giudiceLogin) {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT id, evento_id FROM voto WHERE giudice_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giudiceLogin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getVoto(rs.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Aggiorna il valore di un voto.
     * @param voto voto aggiornato
     * @param id identificativo del voto da aggiornare
     * @return true se l'aggiornamento ha successo, false altrimenti
     */
    @Override
    public boolean aggiornaVoto(Voto voto, int id) {
        String sql = "UPDATE voto SET votazione = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, voto.getVotazione());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un voto dal database tramite identificativo.
     * @param id identificativo del voto da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    @Override
    public boolean eliminaVoto(int id) {
        String sql = "DELETE FROM voto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica se un giudice ha già votato un team per uno specifico evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return true se il giudice ha già votato il team, false altrimenti
     */
    @Override
    public boolean giudiceHaVotatoTeam(String loginGiudice, String nomeTeam, int eventoId) {
        String sql = "SELECT COUNT(*) AS cnt FROM voto WHERE giudice_login = ? AND team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginGiudice);
            ps.setString(2, nomeTeam);
            ps.setInt(3, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("cnt") > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Restituisce il valore del voto assegnato da un giudice ad un team per uno specifico evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return valore del voto, -1 se non trovato
     */
    @Override
    public int getVotoDiGiudiceTeam(String loginGiudice, String nomeTeam, int eventoId) {
        String sql = "SELECT votazione FROM voto WHERE giudice_login = ? AND team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginGiudice);
            ps.setString(2, nomeTeam);
            ps.setInt(3, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("votazione");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1; // Nessun voto trovato
    }

    /**
     * Registra un voto per un team da parte di un giudice in un evento.
     * @param loginGiudice login del giudice
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @param voto valore del voto da assegnare
     */
    @Override
    public void votaTeam(String loginGiudice, String nomeTeam, int eventoId, int voto) {
        String sql = "INSERT INTO voto (giudice_login, team_nome, evento_id, votazione) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginGiudice);
            ps.setString(2, nomeTeam);
            ps.setInt(3, eventoId);
            ps.setInt(4, voto);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Imposta il DAO per la gestione dei giudici.
     * @param giudiceDAO implementazione del DAO giudice
     */
    @Override
    public void setGiudiceDAO(GiudiceDAO giudiceDAO) {
        this.giudiceDAO = giudiceDAO;
    }

}
