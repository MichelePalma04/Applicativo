package implementazione_postgres_dao;

import dao.DocumentoDAO;
import database.ConnessioneDatabase;
import model.*;

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
    private IGiudiceDAO giudiceDAO;
    private IDocumentoDAO documentoDAO;

    private static final String PERCORSO_FILE_COLUMN = "percorso_file";
    private static final String TEAM_NOME_COLUMN = "team_nome";
    private static final String LOGIN_COLUMN = "partecipante_login";

    public IDocumentoDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Documento> getDocumentiTeamEvento(int eventoId, String nomeTeam) {
        List<Documento> docs = new ArrayList<>();
        String query = "SELECT id, data, nome_file, percorso_file, team_nome, evento_id, partecipante_login FROM documento WHERE team_nome = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate data = rs.getDate("data").toLocalDate();
                File file = new File(rs.getString(PERCORSO_FILE_COLUMN));
                String nometeam = rs.getString(TEAM_NOME_COLUMN);
                String loginPartecipante = rs.getString(LOGIN_COLUMN);
                Team team = new Team(nometeam, new ArrayList<>(), new ArrayList<>());
                Documento doc = new Documento(data, file, team, loginPartecipante);
                doc.setId(rs.getInt("id"));
                doc.setCommentiGiudici(getCommentiDocumento(doc.getId(), eventoId));
                docs.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docs;
    }
    @Override
    public Documento getDocumentoById(int idDocumento) {
        String sql = "SELECT id, data, nome_file, percorso_file, team_nome, evento_id, partecipante_login FROM documento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idDocumento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDate data = rs.getDate("data").toLocalDate();
                File file = new File(rs.getString(PERCORSO_FILE_COLUMN));
                String nometeam = rs.getString(TEAM_NOME_COLUMN);
                Team team = new Team(nometeam, new ArrayList<>(), new ArrayList<>());
                String loginPartecipante = rs.getString(LOGIN_COLUMN);
                Documento doc = new Documento(data, file, team, loginPartecipante);
                doc.setId(idDocumento);
                return doc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Salva documento per un team e un evento
    @Override
    public void save(Documento documento, String nomeTeam, int eventoId, String login) {
        String sql = "INSERT INTO documento (data, nome_file, percorso_file, team_nome, evento_id, partecipante_login) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(documento.getData()));
            ps.setString(2, documento.getFile().getName());
            ps.setString(3, documento.getFile().getAbsolutePath());
            ps.setString(4, nomeTeam);
            ps.setInt(5, eventoId);
            ps.setString(6, login);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Restituisce tutti i documenti per un evento
    @Override
    public List<Documento> getDocumentiEvento(int eventoId) {
        List<Documento> docs = new ArrayList<>();
        String sql = "SELECT id, data, nome_file, percorso_file, team_nome, evento_id, partecipante_login FROM documento WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate dataCaricamento = rs.getDate("data_caricamento").toLocalDate();
                String filepath = rs.getString(PERCORSO_FILE_COLUMN);
                File file = new File(filepath);
                String teamNome = rs.getString(TEAM_NOME_COLUMN);
                String loginPartecipante = rs.getString(LOGIN_COLUMN);
                // Ottieni l'oggetto Team tramite il TeamDAO
                Team team = teamDAO.getTeam(teamNome, eventoId);

                Documento doc = new Documento(dataCaricamento, file, team, loginPartecipante);
                doc.setCommentiGiudici(getCommentiDocumento(doc.getId(), eventoId));
                docs.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docs;
    }

    @Override
    public List<Documento> getDocumentiTeamEventoPartecipante(int eventoId, String nomeTeam, String login) {
        List<Documento> docs = new ArrayList<>();
        String query = "SELECT id, data, nome_file, percorso_file, team_nome, evento_id, partecipante_login FROM documento WHERE team_nome = ? AND evento_id = ? AND partecipante_login = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nomeTeam);
            ps.setInt(2, eventoId);
            ps.setString(3, login);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate data = rs.getDate("data").toLocalDate();
                File file = new File(rs.getString(PERCORSO_FILE_COLUMN));
                String nometeam = rs.getString(TEAM_NOME_COLUMN);
                String loginPartecipante = rs.getString(LOGIN_COLUMN);
                Team team = new Team(nometeam, new ArrayList<>(), new ArrayList<>());
                Documento doc = new Documento(data, file, team, loginPartecipante);
                doc.setId(rs.getInt("id"));
                doc.setCommentiGiudici(getCommentiDocumento(doc.getId(), eventoId));
                docs.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docs;
    }

    @Override
    public List<CommentoGiudice> getCommentiDocumento(int idDocumento, int eventoId) {
        List<CommentoGiudice> commenti = new ArrayList<>();
        String sql = "SELECT utente_login, testo_commento FROM commento_documento WHERE id_documento = ? AND evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idDocumento);
            ps.setInt(2, eventoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String loginGiudice = rs.getString("utente_login");
                String testo = rs.getString("testo_commento");
                // Recupera il Giudice tramite il suo DAO (esempio)
                Giudice giudice = giudiceDAO.getGiudice(loginGiudice, eventoId);
                // Recupera il Documento tramite il suo DAO (esempio)
                Documento documento = documentoDAO.getDocumentoById(idDocumento);

                commenti.add(new CommentoGiudice(giudice, documento, testo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commenti;
    }

    @Override
    public boolean aggiungiCommentoGiudice (int documentoId, String utenteLogin, int eventoId, String testoCommento) {
        String sql = "INSERT INTO commento_documento (id_documento, utente_login, evento_id, testo_commento) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, documentoId);
            ps.setString(2, utenteLogin);
            ps.setInt(3, eventoId);
            ps.setString(4, testoCommento);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setTeamDAO (ITeamDAO teamDAO){
        this.teamDAO = teamDAO;
    }

    @Override
    public void setGiudiceDAO (IGiudiceDAO giudiceDAO){
        this.giudiceDAO = giudiceDAO;
    }

    @Override
    public void setDocumentoDAO(IDocumentoDAO documentoDAO){
        this.documentoDAO = documentoDAO;
    }
}

