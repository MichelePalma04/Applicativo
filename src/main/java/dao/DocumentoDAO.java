package dao;

import model.Documento;

public interface DocumentoDAO {
    void save(Documento documento, String nomeTeam, int eventoId);
}
