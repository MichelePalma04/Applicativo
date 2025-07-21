package implementazionePostgresDAO;
import dao.PartecipanteDAO;
import database.ConnessioneDatabase;
import model.Evento;
import model.Partecipante;
import model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IPatecipanteDAO implements PartecipanteDAO{

    private Connection connection;

    public IPatecipanteDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch(SQLException e){
            System.out.println("Errore nella connessione al database: "+ e.getMessage());
        }
    }

    public Partecipante getPartecipante(String utenteLogin, String teamNome, int eventoId) {
        // 1. Prendi password da utente
        String password = null;
        String sqlUtente = "SELECT password FROM utente WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlUtente)) {
            ps.setString(1, utenteLogin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                password = rs.getString("password");
            }
        } catch (SQLException e) {
            // gestione errore
        }

        // 2. Prendi oggetto Team
        Team team = teamDAO.getTeam(teamNome, eventoId);

        // 3. Prendi oggetto Evento
        Evento evento = eventoDAO.getEvento(eventoId);
        ArrayList<Evento> eventi = new ArrayList<>();
        eventi.add(evento);

        // 4. Costruisci Partecipante
        return new Partecipante(utenteLogin, password, team, eventi);
    }

    @Override
    public List<Partecipante> getPartecipantiEvento(int eventoId) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT * FROM partecipante WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Partecipante(rs.getString("utente_login"), rs.getInt("evento_id"), rs.getString("team_nome")));
            }
        } catch (SQLException e) {
            System.out.println("Errore nel recupero partecipanti: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Partecipante> getPartecipantiUtente(String utenteLogin) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT * FROM partecipante WHERE utente_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, utenteLogin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Partecipante(rs.getString("utente_login"), rs.getInt("evento_id"), rs.getString("team_nome")));
            }
        } catch (SQLException e) {
            System.out.println("Errore nel recupero partecipanti per utente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean aggiungiPartecipante(String utenteLogin, int eventoId, String teamNome) {
        String sql = "INSERT INTO partecipante (utente_login, evento_id, team_nome) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, utenteLogin);
            ps.setInt(2, eventoId);
            ps.setString(3, teamNome);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore nell'inserimento partecipante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminaPartecipante(String utenteLogin, int eventoId) {
        String sql = "DELETE FROM partecipante WHERE utente_login = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, utenteLogin);
            ps.setInt(2, eventoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore nell'eliminazione partecipante: " + e.getMessage());
            return false;
        }
    }
}
}
