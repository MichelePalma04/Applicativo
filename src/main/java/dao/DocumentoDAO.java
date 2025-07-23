package dao;

import implementazionePostgresDAO.ITeamDAO;
import model.Documento;

import java.util.List;

public interface DocumentoDAO {
    void save(Documento documento, String nomeTeam, int eventoId);
    boolean teamHaDocumenti (String nomeTeam, int eventoId);
    List<Documento> getDocumentiEvento(int eventoId);
    List<Documento> getDocumentiTeamEvento(int eventoId, String nomeTeam);
    void setTeamDAO (ITeamDAO teamDAO);
}
