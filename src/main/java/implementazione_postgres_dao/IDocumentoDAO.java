package implementazione_postgres_dao;

import dao.DocumentoDAO;
import dao.GiudiceDAO;
import dao.TeamDAO;
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

/**
 * Implementazione Postgres del DAO per la gestione dei documenti.
 * Fornisce metodi per il salvataggio, recupero e commento dei documenti associati a team e eventi.
 */
public class IDocumentoDAO implements DocumentoDAO {
    /** Connessione al database. */
    private Connection connection;

    /** DAO per la gestione dei team. */
    private TeamDAO teamDAO;

    /** DAO per la gestione dei giudici. */
    private GiudiceDAO giudiceDAO;

    /** DAO per la gestione dei documenti (self-reference). */
    private DocumentoDAO documentoDAO;

    /** Nome della colonna percorso file nella tabella documento. */
    private static final String PERCORSO_FILE_COLUMN = "percorso_file";

    /** Nome della colonna team_nome nella tabella documento. */
    private static final String TEAM_NOME_COLUMN = "team_nome";

    /** Nome della colonna partecipante_login nella tabella documento. */
    private static final String LOGIN_COLUMN = "partecipante_login";

    /**
     * Costruttore. Inizializza la connessione al database.
     */
    public IDocumentoDAO() {
        try{
            connection = ConnessioneDatabase.getInstance().connection;
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Restituisce i documenti di un team per un evento.
     * @param eventoId identificativo dell'evento
     * @param nomeTeam nome del team
     * @return lista di documenti associati al team e all'evento
     */
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

    /**
     * Restituisce il documento tramite identificativo.
     * @param idDocumento identificativo del documento
     * @return documento trovato, o null se non esiste
     */
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

    /**
     * Salva un documento relativo a un team e a un evento.
     * @param documento documento da salvare
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @param login login del partecipante che carica il documento
     */
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

    /**
     * Verifica se il team ha documenti per uno specifico evento.
     * @param nomeTeam nome del team
     * @param eventoId identificativo dell'evento
     * @return true se il team ha documenti per l'evento, false altrimenti.
     */
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

    /**
     * Restituisce tutti i documenti di un evento.
     * @param eventoId identificativo dell'evento
     * @return lista di documenti associati all'evento
     */
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

    /**
     * Restituisce i documenti di un team per un evento e un partecipante.
     * @param eventoId identificativo dell'evento
     * @param nomeTeam nome del team
     * @param login login del partecipante
     * @return lista di documenti associati al team, evento e partecipante
     */
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

    /**
     * Restituisce la lista dei commenti dei giudici per un documento e un evento.
     * @param idDocumento identificativo del documento
     * @param eventoId identificativo dell'evento
     * @return lista dei commenti dei giudici
     */
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

    /**
     * Aggiunge un commento di un giudice ad un documento associato ad un evento.
     * @param documentoId identificativo del documento
     * @param utenteLogin login del giudice
     * @param eventoId identificativo dell'evento
     * @param testoCommento testo del commento da aggiungere
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     */
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

    /**
     * Imposta il DAO per la gestione dei team.
     * @param teamDAO implementazione del TeamDAO
     */
    @Override
    public void setTeamDAO (TeamDAO teamDAO){
        this.teamDAO = teamDAO;
    }

    /**
     * Imposta il DAO per la gestione dei giudici.
     * @param giudiceDAO implementazione del GiudiceDAO
     */
    @Override
    public void setGiudiceDAO (GiudiceDAO giudiceDAO){
        this.giudiceDAO = giudiceDAO;
    }

    /**
     * Imposta il DAO per la gestione dei documenti (self-reference).
     * @param documentoDAO implementazione del DocumentoDAO
     */
    @Override
    public void setDocumentoDAO(DocumentoDAO documentoDAO){
        this.documentoDAO = documentoDAO;
    }
}

