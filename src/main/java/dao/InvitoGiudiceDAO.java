package dao;

import implementazione_postgres_dao.IEventoDAO;
import implementazione_postgres_dao.IUtenteDAO;
import model.InvitoGiudice;

import java.util.List;

public interface InvitoGiudiceDAO {
    boolean addInvitoGiudice(InvitoGiudice invito);
    boolean updateInvitoGiudice(InvitoGiudice invito);
    boolean esisteInvitoPendentePerUtenteEvento(String login, int eventoId);
    boolean deleteInvitoGiudice(int idInvito);
    InvitoGiudice getInvitoById(int idInvito);
    List<InvitoGiudice> getInvitiPendentiPerUtente(String loginUtente);
    void setUtenteDAO(IUtenteDAO utenteDAO);
    void setEventoDAO(IEventoDAO eventoDAO);
}
