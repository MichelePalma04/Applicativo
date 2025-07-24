package dao;

import implementazionePostgresDAO.IDocumentoDAO;
import implementazionePostgresDAO.IGiudiceDAO;
import implementazionePostgresDAO.ITeamDAO;
import model.CommentoGiudice;
import model.Documento;

import java.util.List;

public interface DocumentoDAO {
    Documento getDocumentoById(int idDocumento);
    void save(Documento documento, String nomeTeam, int eventoId, String login);
    boolean teamHaDocumenti (String nomeTeam, int eventoId);
    List<Documento> getDocumentiEvento(int eventoId);
    List<Documento> getDocumentiTeamEventoPartecipante(int eventoId, String nomeTeam, String login);
    List<Documento> getDocumentiTeamEvento(int eventoId, String nomeTeam);
    void setTeamDAO (ITeamDAO teamDAO);
    List<CommentoGiudice> getCommentiDocumento(int idDocumento, int eventoId);
    boolean aggiungiCommentoGiudice (int idDocumento, String utenteLogin, int eventoId, String testoCommento);
    void setGiudiceDAO (IGiudiceDAO giudiceDAO);
    void setDocumentoDAO (IDocumentoDAO documentoDAO);
}
