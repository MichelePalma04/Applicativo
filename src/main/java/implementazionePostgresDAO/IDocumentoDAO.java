package implementazionePostgresDAO;

import dao.DocumentoDAO;
import database.ConnessioneDatabase;
import model.Documento;
import model.Team;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IDocumentoDAO implements DocumentoDAO {
    private Connection connection;
    private ITeamDAO teamDAO;

    public IDocumentoDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch (SQLException e){
            System.out.println("Errore nella connessione al database: " + e.getMessage());
        }
    }

    // Salva documento per un team e un evento
    @Override
    public void save(Documento documento, String nomeTeam, int eventoId) {
        String sql = "INSERT INTO documento (data, nome_file, percorso_file, team_nome, evento_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(documento.getData()));
            ps.setString(2, documento.getFile().getName());
            ps.setString(3, documento.getFile().getAbsolutePath());
            ps.setString(4, nomeTeam);
            ps.setInt(5, eventoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore nel salvataggio documento: " + e.getMessage());
        }
    }

    // Verifica se il team ha documenti per l'evento
    @Override
    public boolean teamHaDocumenti(String nomeTeam, int eventoId) {
        String sql = "SELECT COUNT(*) AS cnt FROM documento WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("cnt") > 0) return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Restituisce tutti i documenti per un evento
    @Override
    public List<Documento> getDocumentiEvento(int eventoId) {
        List<Documento> docs = new ArrayList<>();
        String sql = "SELECT * FROM documento WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate dataCaricamento = rs.getDate("data_caricamento").toLocalDate();
                String filepath = rs.getString("filepath");
                File file = new File(filepath);
                String teamNome = rs.getString("team_nome");

                // Ottieni l'oggetto Team tramite il TeamDAO
                Team team = teamDAO.getTeam(teamNome, eventoId);

                Documento doc = new Documento(dataCaricamento, file, team);
                docs.add(doc);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return docs;
    }

    @Override
    public List<Documento> getDocumentiTeamEvento(int eventoId, String nomeTeam) {
        List<Documento> docs = new ArrayList<>();
        String query = "SELECT * FROM documento WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate data = rs.getDate("data").toLocalDate();
                File file = new File(rs.getString("path_file"));
                String nometeam = rs.getString("team_nome");
                Team team = new Team(nometeam, new ArrayList<>(), new ArrayList<>());
                Documento doc = new Documento(data, file, team);
                docs.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docs;
    }

    @Override
    public void setTeamDAO (ITeamDAO teamDAO){
        this.teamDAO = teamDAO;
    }
}

