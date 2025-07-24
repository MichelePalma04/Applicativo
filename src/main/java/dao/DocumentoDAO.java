package dao;

import implementazionePostgresDAO.ITeamDAO;
import model.CommentoGiudice;
import model.Documento;

import java.util.List;

public interface DocumentoDAO {
    void save(Documento documento, String nomeTeam, int eventoId);
    boolean teamHaDocumenti (String nomeTeam, int eventoId);
    List<Documento> getDocumentiEvento(int eventoId);
    List<Documento> getDocumentiTeamEvento(int eventoId, String nomeTeam);
    void setTeamDAO (ITeamDAO teamDAO);
    List<CommentoGiudice> getCommentiDocumento(int idDocumento, int eventoId);
    boolean aggiungiCommentoGiudice (int idDocumento, String utenteLogin, int eventoId, String testoCommento);
}
