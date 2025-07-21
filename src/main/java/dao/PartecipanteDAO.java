package dao;

import model.Partecipante;
import java.util.List;

public interface PartecipanteDAO {
    Partecipante getPartecipante(String utenteLogin, int eventoId);
    List<Partecipante> getPartecipantiEvento(int eventoId);
    List<Partecipante> getPartecipantiUtente(String utenteLogin);
    boolean aggiungiPartecipante(String utenteLogin, int eventoId, String teamNome);
    boolean eliminaPartecipante(String utenteLogin, int eventoId);
}
