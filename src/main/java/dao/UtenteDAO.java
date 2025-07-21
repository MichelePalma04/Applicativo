package dao;

import model.Utente;

import java.util.List;

public interface UtenteDAO {
    Utente getUtentebyLogin(String login);
    Utente getUtentebyLoginAndPassword(String login, String password);
    List<Utente> getAllUtenti();
    boolean addUtente(Utente utente);
    boolean updateUtente(Utente utente);
    boolean deleteUtente(String login);
}
