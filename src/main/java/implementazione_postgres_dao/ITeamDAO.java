package implementazione_postgres_dao;
import dao.PartecipanteDAO;
import dao.TeamDAO;
import dao.VotoDAO;
import model.Partecipante;
import model.Team;
import database.ConnessioneDatabase;
import model.Voto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Postgres del DAO per la gestione dei team.
 * Fornisce metodi per il recupero, aggiunta, eliminazione e gestione dei partecipanti e voti all'interno dei team associati agli eventi.
 */
public class ITeamDAO implements TeamDAO {

    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione dei partecipanti. */
    private PartecipanteDAO partecipanteDAO;

    /** DAO per la gestione dei voti. */
    private VotoDAO votoDAO;

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public ITeamDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce il team associato al nome e all'evento specificato.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return Team trovato oppure null se non esiste
     */
    @Override
    public Team getTeam(String nomeTeam, int eventoId) {
        String sql = "SELECT nome_team, evento_id FROM team WHERE nome_team = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ArrayList<Partecipante> partecipanti = new ArrayList<>(partecipanteDAO.getPartecipantiTeam(nomeTeam, eventoId));
                ArrayList <Voto> voto = new ArrayList <>(votoDAO.getVotiTeam(nomeTeam, eventoId));
                return new Team(nomeTeam, partecipanti, voto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Restituisce la lista di tutti i team associati ad un evento.
     * @param eventoId identificativo dell'evento
     * @return lista dei team dell'evento
     */
    @Override
    public List<Team> getTeamEvento(int eventoId) {
        List<Team> lista = new ArrayList<>();
        String sql = "SELECT nome_team FROM team WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(getTeam(rs.getString("nome_team"), eventoId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Aggiunge un nuovo team ad un evento.
     * @param team team da aggiungere
     * @param eventoId identificativo dell'evento
     * @return true se l'inserimento ha successo, false altrimenti
     */
    @Override
    public boolean aggiungiTeam(Team team, int eventoId) {
        String sql = "INSERT INTO team (nome_team, evento_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, team.getNomeTeam());
            ps.setInt(2, eventoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se un partecipante appartiene a un team per un evento specifico.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return true se il partecipante è nel team, false altrimenti
     */
    @Override
    public boolean isPartecipanteInTeam(String loginPartecipante, String nomeTeam, int eventoId) {
        String sql = "SELECT COUNT(*) FROM team_partecipante WHERE team_nome = ? AND evento_id = ? AND utente_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ps.setString(3, loginPartecipante);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Restituisce la dimensione (numero di partecipanti) di un team per un evento specifico.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return dimensione del team
     */
    @Override
    public int getDimTeam(String nomeTeam, int eventoId) {
        String sql = "SELECT COUNT(*) FROM team_partecipante WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Elimina un team associato ad un evento.
     * @param nomeTeam nome del team da eliminare
     * @param eventoId identificativo dell'evento
     * @return true se l'eliminazione ha successo, false altrimenti
     */
    @Override
    public boolean eliminaTeam(String nomeTeam, int eventoId) {
        String sql = "DELETE FROM team WHERE nome_team = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aggiunge un partecipante ad un team.
     * @param loginPartecipante login del partecipante
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     */
    @Override
    public void unisciPartecipanteATeam(String loginPartecipante, String nomeTeam, int eventoId) {
        String sql = "INSERT INTO team_partecipante (team_nome, evento_id, utente_login) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ps.setString(3, loginPartecipante);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imposta il DAO per la gestione dei voti.
     * @param votoDAO implementazione del DAO voto
     */
    @Override
    public void setVotoDAO(VotoDAO votoDAO) {
        this.votoDAO = votoDAO;
    }

    /**
     * Imposta il DAO per la gestione dei partecipanti.
     * @param partecipanteDAO implementazione del DAO partecipante
     */
    @Override
    public void setPartecipanteDAO(PartecipanteDAO partecipanteDAO) {
        this.partecipanteDAO = partecipanteDAO;
    }

}
