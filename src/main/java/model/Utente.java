package model;

/**
 * Rappresenta un utente generico del sistema.
 * Un utente è identificato da un login e una password.
 * Questa classe può essere estesa da altri tipi di utenti (Organizzatore, Giudice, Partecipante).
 */
public class Utente {
    /** Login dell'utente. */
    private String login;

    /** Password dell'utente. */
    private String password;

    /**
     * Costruttore della classe Utente.
     * @param login Login dell'utente
     * @param password Password dell'utente
     */
    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Restituisce il login dell'utente.
     * @return login dell'utente
     */
    public String getLogin() {
        return login;
    }

    /**
     * Restituisce la password dell'utente.
     * @return password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Restituisce una rappresentazione testuale dell'utente (il login).
     * @return login dell'utente
     */
    @Override
    public String toString(){
        return login;
    }
}
