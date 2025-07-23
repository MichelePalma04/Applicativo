package implementazionePostgresDAO;

import dao.DocumentoDAO;
import database.ConnessioneDatabase;
import model.Documento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IDocumentoDAO implements DocumentoDAO {
    private Connection connection;

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
}
